package kr.co.csalgo.application.mail.usecase;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.domain.email.EmailSender;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.entity.ResponseFeedback;
import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.domain.question.service.QuestionResponseService;
import kr.co.csalgo.domain.question.service.ResponseFeedbackService;
import kr.co.csalgo.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
class SendFeedbackMailUseCaseTest {

	@Mock
	QuestionResponseService questionResponseService;
	@Mock
	ResponseFeedbackService responseFeedbackService;
	@Mock
	FeedbackAnalyzer feedbackAnalyzer;
	@Mock
	EmailSender emailSender;

	@InjectMocks
	SendFeedbackMailUseCase sendFeedbackMailUseCase;

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
			.summary("좋은 시도입니다. 하지만 개선이 필요합니다.")
			.similarity(0.8)
			.build();
		when(feedbackAnalyzer.analyze(anyString(), anyString(), anyString()))
			.thenReturn(feedbackResult);

		ResponseFeedback savedFeedback = mock(ResponseFeedback.class);
		when(savedFeedback.getId()).thenReturn(10L);
		when(responseFeedbackService.create(any(), any())).thenReturn(savedFeedback);

		// when
		sendFeedbackMailUseCase.execute();

		// then
		verify(questionResponseService, times(1)).list();
		verify(responseFeedbackService, times(1)).create(response, feedbackResult.getSummary());
		verify(emailSender, times(1)).sendReply(
			eq("team.jjins@gmail.com"),
			contains("트랜잭션"), // 제목에 questionTitle 포함
			anyString(),        // 메일 본문
			eq("<original-message-id@example.com>")
		);
	}

	@Test
	@DisplayName("이미 피드백이 존재하는 응답은 스킵한다")
	void testSkipIfFeedbackAlreadyExists() {
		QuestionResponse response = mock(QuestionResponse.class);
		when(questionResponseService.list()).thenReturn(List.of(response));
		when(responseFeedbackService.isFeedbackExists(response)).thenReturn(true);

		sendFeedbackMailUseCase.execute();

		verify(responseFeedbackService, never()).create(any(), any());
		verify(emailSender, never()).sendReply(any(), any(), any(), any());
	}

	@Test
	@DisplayName("한 사용자 메일 전송 실패해도 다른 사용자에게는 정상 발송된다")
	void testOneMailFailsOthersStillSent() {
		User failUser = User.builder().email("fail@test.com").build();
		Question question = Question.builder().title("Q").solution("정답").build();

		QuestionResponse failResponse = QuestionResponse.builder()
			.question(question)
			.messageId("msg-1")
			.content("답변1")
			.user(failUser)
			.build();

		User okUser = User.builder().email("ok@test.com").build();
		QuestionResponse okResponse = QuestionResponse.builder()
			.question(question)
			.messageId("msg-2")
			.content("답변2")
			.user(okUser)
			.build();

		when(questionResponseService.list()).thenReturn(List.of(failResponse, okResponse));
		when(responseFeedbackService.isFeedbackExists(any())).thenReturn(false);

		FeedbackResult feedbackResult = FeedbackResult.builder()
			.summary("피드백")
			.similarity(0.9)
			.build();
		when(feedbackAnalyzer.analyze(anyString(), anyString(), anyString()))
			.thenReturn(feedbackResult);

		when(responseFeedbackService.create(any(), any()))
			.thenReturn(mock(ResponseFeedback.class));

		doThrow(new RuntimeException("메일 실패"))
			.when(emailSender).sendReply(eq("fail@test.com"), any(), any(), any());

		sendFeedbackMailUseCase.execute();

		verify(emailSender).sendReply(eq("fail@test.com"), any(), any(), any());
		verify(emailSender).sendReply(eq("ok@test.com"), any(), any(), any());
	}
}
