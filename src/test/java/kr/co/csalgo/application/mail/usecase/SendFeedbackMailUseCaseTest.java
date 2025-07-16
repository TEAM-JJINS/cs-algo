package kr.co.csalgo.application.mail.usecase;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import kr.co.csalgo.email.EmailSender;
import kr.co.csalgo.question.entity.Question;
import kr.co.csalgo.question.entity.QuestionResponse;
import kr.co.csalgo.question.entity.ResponseFeedback;
import kr.co.csalgo.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.question.feedback.FeedbackResult;
import kr.co.csalgo.question.service.QuestionResponseService;
import kr.co.csalgo.question.service.ResponseFeedbackService;
import kr.co.csalgo.user.entity.User;

@SpringBootTest(classes = SendFeedbackMailUseCase.class)
class SendFeedbackMailUseCaseTest {

	@Autowired
	private SendFeedbackMailUseCase sendFeedbackMailUseCase;

	@MockitoBean
	private QuestionResponseService questionResponseService;

	@MockitoBean
	private ResponseFeedbackService responseFeedbackService;

	@MockitoBean
	private FeedbackAnalyzer feedbackAnalyzer;

	@MockitoBean
	private EmailSender emailSender;

	@Test
	@DisplayName("피드백이 존재하지 않는 응답에 대해 피드백 생성 및 메일 전송이 수행된다")
	void sendFeedbackMailSuccessfully() {
		// given
		User user = User.builder()
			.email("team.jjins@gmail.com")
			.build();

		Question question = Question.builder()
			.title("트랜잭션")
			.solution("정답입니다")
			.build();

		QuestionResponse response = QuestionResponse.builder()
			.question(question)
			.messageId("<original-message-id@example.com>")
			.content("제 답변입니다")
			.user(user)
			.build();

		when(questionResponseService.list()).thenReturn(List.of(response));
		when(responseFeedbackService.isFeedbackExists(response)).thenReturn(false);

		FeedbackResult feedbackResult = FeedbackResult.builder()
			.responseContent("좋은 시도입니다. 하지만 개선이 필요합니다.")
			.questionSolution("정답입니다")
			.build();
		when(feedbackAnalyzer.analyze(any(), any())).thenReturn(feedbackResult);

		ResponseFeedback savedFeedback = mock(ResponseFeedback.class);
		when(savedFeedback.getId()).thenReturn(10L);
		when(responseFeedbackService.create(any(), any())).thenReturn(savedFeedback);

		// when
		sendFeedbackMailUseCase.execute();

		// then
		verify(questionResponseService, times(1)).list();
		verify(responseFeedbackService, times(1)).create(response, feedbackResult.getResponseContent());
	}
}
