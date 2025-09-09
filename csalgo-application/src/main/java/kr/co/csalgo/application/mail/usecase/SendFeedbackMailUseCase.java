package kr.co.csalgo.application.mail.usecase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.csalgo.common.util.MailTemplate;
import kr.co.csalgo.domain.email.EmailSender;
import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.entity.ResponseFeedback;
import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.domain.question.service.QuestionResponseService;
import kr.co.csalgo.domain.question.service.ResponseFeedbackSendingHistoryService;
import kr.co.csalgo.domain.question.service.ResponseFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SendFeedbackMailUseCase {
	private final QuestionResponseService questionResponseService;
	private final ResponseFeedbackService responseFeedbackService;
	private final ResponseFeedbackSendingHistoryService responseFeedbackSendingHistoryService;
	private final FeedbackAnalyzer feedbackAnalyzer;
	private final EmailSender emailSender;

	public void execute() {
		List<QuestionResponse> feedbackResponses = questionResponseService.list().stream()
			.filter(response -> !responseFeedbackService.isFeedbackExists(response))
			.toList();

		int successCount = 0;
		int failCount = 0;

		for (QuestionResponse response : feedbackResponses) {
			try {
				FeedbackResult feedbackResult = feedbackAnalyzer.analyze(response.getContent(), response.getQuestion().getSolution());

				ResponseFeedback result = responseFeedbackService.create(response, feedbackResult.getResponseContent());

				if (responseFeedbackSendingHistoryService.isSent(result, LocalDate.now())) {
					log.info("[문제 전송 스킵] 이미 발송된 피드백: responseFeedbackId{}", result.getId());
					continue;
				}

				emailSender.sendReply(
					response.getUser().getEmail(),
					MailTemplate.FEEDBACK_MAIL_SUBJECT_REPLY.formatted(response.getQuestion().getTitle()),
					MailTemplate.formatFeedbackMailBody(
						response.getUser().getEmail().split("@")[0],
						response.getQuestion().getTitle(),
						feedbackResult.getResponseContent(),
						feedbackResult.getQuestionSolution(),
						feedbackResult.getSimilarity(),
						feedbackResult.getGuideMessage()
					),
					response.getMessageId());
				log.info("피드백 메일 전송 성공: responseId={}, feedbackId={}", response.getId(), result.getId());
				responseFeedbackSendingHistoryService.create(result);
				successCount++;
			} catch (Exception e) {
				log.error("피드백 메일 전송 실패: responseId={}, error={}", response.getId(), e.getMessage(), e);
				failCount++;
			}
		}

		log.info("피드백 메일 전송 완료: 성공 {}건, 실패 {}건", successCount, failCount);
	}
}
