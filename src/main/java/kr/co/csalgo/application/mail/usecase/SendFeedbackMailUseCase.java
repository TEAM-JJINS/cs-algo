package kr.co.csalgo.application.mail.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.csalgo.common.util.MailTemplate;
import kr.co.csalgo.domain.email.EmailSender;
import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.entity.ResponseFeedback;
import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.domain.question.service.QuestionResponseService;
import kr.co.csalgo.domain.question.service.ResponseFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendFeedbackMailUseCase {
	private final QuestionResponseService questionResponseService;
	private final ResponseFeedbackService responseFeedbackService;
	private final FeedbackAnalyzer feedbackAnalyzer;
	private final EmailSender emailSender;

	public void execute() {
		List<QuestionResponse> responses = questionResponseService.list();
		List<QuestionResponse> feedbackResponses = responses.stream()
			.filter(response -> !responseFeedbackService.isFeedbackExists(response))
			.toList();

		int successCount = 0;
		int failCount = 0;

		for (QuestionResponse response : feedbackResponses) {
			try {
				ResponseFeedback result = responseFeedbackService.create(response, "피드백 내용");

				FeedbackResult feedbackResult = feedbackAnalyzer.analyze(response.getContent(), response.getQuestion().getSolution());

				emailSender.send(
					response.getUser().getEmail(),
					MailTemplate.FEEDBACK_MAIL_SUBJECT.formatted(response.getQuestion().getTitle()),
					MailTemplate.formatFeedbackMailBody(
						response.getUser().getEmail().split("@")[0],
						feedbackResult.getResponseContent(),
						feedbackResult.getQuestionSolution()
					));
				log.info("피드백 메일 전송 성공: responseId={}, feedbackId={}", response.getId(), result.getId());
				successCount++;
			} catch (Exception e) {
				log.error("피드백 메일 전송 실패: responseId={}, error={}", response.getId(), e.getMessage(), e);
				failCount++;
			}
		}

		// TODO: 3. 피드백 메일 처리에 대한 로깅
	}
}
