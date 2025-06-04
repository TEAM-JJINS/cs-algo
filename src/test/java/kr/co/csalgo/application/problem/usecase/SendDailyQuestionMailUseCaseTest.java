package kr.co.csalgo.application.problem.usecase;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.service.QuestionSendingHistoryService;
import kr.co.csalgo.domain.question.service.QuestionService;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;
import kr.co.csalgo.infrastructure.email.service.EmailService;

@DisplayName("SendDailyQuestionMailUseCase 테스트")
class SendDailyQuestionMailUseCaseTest {
	@Mock
	private QuestionSendingHistoryService questionSendingHistoryService;
	@Mock
	private QuestionService questionService;
	@Mock
	private UserService userService;
	@Mock
	private EmailService emailService;

	@InjectMocks
	SendDailyQuestionMailUseCase sendDailyQuestionMailUseCase;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		sendDailyQuestionMailUseCase = new SendDailyQuestionMailUseCase(
			userService,
			questionService,
			questionSendingHistoryService,
			emailService
		);
	}

	@Test
	@DisplayName("전체 사용자에게 문제를 전송한다.")
	void testExecuteSuccess() {
		User user1 = createUser(1L, "u1@test.com");
		User user2 = createUser(2L, "u2@test.com");

		Question q1 = createQuestion(101L, "Q1");
		Question q2 = createQuestion(102L, "Q2");

		when(questionService.list()).thenReturn(List.of(q1, q2));
		when(userService.list()).thenReturn(List.of(user1, user2));
		when(questionSendingHistoryService.count(user1)).thenReturn(0L);
		when(questionSendingHistoryService.count(user2)).thenReturn(1L);

		sendDailyQuestionMailUseCase.execute();

		verify(emailService).sendEmail(eq("u1@test.com"), contains("Q1"), any());
		verify(emailService).sendEmail(eq("u2@test.com"), contains("Q2"), any());
		verify(questionSendingHistoryService).create(101L, 1L);
		verify(questionSendingHistoryService).create(102L, 2L);
	}

	@Test
	@DisplayName("질문이 없으면 전체 사용자에게 메일을 보내지 않는다")
	void testNoQuestions() {
		when(questionService.list()).thenReturn(List.of());

		sendDailyQuestionMailUseCase.execute();

		verify(userService, never()).list();
		verify(emailService, never()).sendEmail(any(), any(), any());
	}

	@Test
	@DisplayName("한 명에게 메일 전송 실패해도 다른 사용자에게는 정상 발송한다.")
	void testExecute_EmailFailForOneUser_OthersStillReceive_Failure() {
		User user1 = createUser(1L, "fail@test.com");
		User user2 = createUser(2L, "ok@test.com");
		Question question = createQuestion(200L, "질문");

		when(questionService.list()).thenReturn(List.of(question));
		when(userService.list()).thenReturn(List.of(user1, user2));
		when(questionSendingHistoryService.count(any())).thenReturn(0L);

		doThrow(new RuntimeException("메일 전송 실패"))
			.when(emailService).sendEmail(eq("fail@test.com"), any(), any());

		sendDailyQuestionMailUseCase.execute();

		verify(emailService).sendEmail(eq("fail@test.com"), any(), any());
		verify(emailService).sendEmail(eq("ok@test.com"), any(), any());
		verify(questionSendingHistoryService).create(200L, 2L);
	}

	private User createUser(Long id, String email) {
		User user = User.builder()
			.email(email)
			.build();
		ReflectionTestUtils.setField(user, "id", id);
		return user;
	}

	private Question createQuestion(Long id, String title) {
		Question question = Question.builder()
			.title(title)
			.description("description")
			.solution("solution")
			.build();
		ReflectionTestUtils.setField(question, "id", id);
		return question;
	}
}
