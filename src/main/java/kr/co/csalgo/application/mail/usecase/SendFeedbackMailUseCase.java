package kr.co.csalgo.application.mail.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.service.QuestionResponseService;
import kr.co.csalgo.domain.question.service.ResponseFeedbackService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SendFeedbackMailUseCase {
	private final QuestionResponseService questionResponseService;
	private final ResponseFeedbackService responseFeedbackService;

	public void execute() {
		List<QuestionResponse> responses = questionResponseService.list();
		List<QuestionResponse> feedbackResponses = responses.stream()
			.filter(response -> !responseFeedbackService.isFeedbackExists(response))
			.toList();

		// TODO: 2. 피드백이 전송되지 않은 문제에 대해 피드백 메일 전송
		// TODO: 3. 피드백 메일 처리에 대한 로깅
	}
}
