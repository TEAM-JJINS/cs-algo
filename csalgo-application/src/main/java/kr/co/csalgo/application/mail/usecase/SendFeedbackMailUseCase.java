package kr.co.csalgo.application.mail.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.csalgo.common.util.MailTemplate;
import kr.co.csalgo.domain.ai.AiClient;
import kr.co.csalgo.domain.email.EmailSender;
import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.entity.ResponseFeedback;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.domain.question.service.QuestionResponseService;
import kr.co.csalgo.domain.question.service.ResponseFeedbackService;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SendFeedbackMailUseCase {

	private final QuestionResponseService questionResponseService;
	private final ResponseFeedbackService responseFeedbackService;
	private final AiClient aiClient; // GptClient
	private final SimilarityCalculator similarityCalculator; // HybridSimilarityCalculator
	private final EmailSender emailSender;

	public void execute() {
		List<QuestionResponse> feedbackResponses = questionResponseService.list().stream()
			.filter(response -> !responseFeedbackService.isFeedbackExists(response))
			.toList();

		int successCount = 0;
		int failCount = 0;

		for (QuestionResponse response : feedbackResponses) {
			try {
				String questionTitle = response.getQuestion().getTitle();
				String solution = response.getQuestion().getSolution();
				String userAnswer = response.getContent();
				String username = response.getUser().getEmail().split("@")[0];

				// 유사도 점수 계산
				double similarityScore = similarityCalculator.calculate(userAnswer, solution);

				// GPT 피드백 생성
				FeedbackResult feedbackResult = aiClient.ask(
					questionTitle,
					solution,
					userAnswer,
					similarityScore
				);

				// DB 저장
				ResponseFeedback result = responseFeedbackService.create(response, feedbackResult.getSummary());

				// 메일 발송
				emailSender.sendReply(
					response.getUser().getEmail(),
					MailTemplate.FEEDBACK_MAIL_SUBJECT_REPLY.formatted(questionTitle),
					MailTemplate.formatFeedbackMailBody(
						username,
						questionTitle,
						userAnswer,
						solution,
						similarityScore,
						feedbackResult.getSummary(),
						feedbackResult.getStrengths(),
						feedbackResult.getImprovements(),
						feedbackResult.getLearningTips()
					),
					response.getMessageId()
				);

				log.info("피드백 메일 전송 성공: responseId={}, feedbackId={}", response.getId(), result.getId());
				successCount++;
			} catch (Exception e) {
				log.error("피드백 메일 전송 실패: responseId={}, error={}", response.getId(), e.getMessage(), e);
				failCount++;
			}
		}

		log.info("피드백 메일 전송 완료: 성공 {}건, 실패 {}건", successCount, failCount);
	}
}
