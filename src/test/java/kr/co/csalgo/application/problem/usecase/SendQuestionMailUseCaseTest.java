package kr.co.csalgo.application.problem.usecase;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.domain.email.EmailSender;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.service.QuestionSendingHistoryService;
import kr.co.csalgo.domain.question.service.QuestionService;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;

@DisplayName("SendQuestionMailUseCase Test")
class SendQuestionMailUseCaseTest {

	@Mock
	private QuestionSendingHistoryService questionSendingHistoryService;
	@Mock
	private QuestionService questionService;
	@Mock
	private UserService userService;
	@Mock
	private EmailSender emailService;
	@Mock
	private ScheduledExecutorService scheduler;

	@InjectMocks
	private SendQuestionMailUseCase useCase;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		useCase = new SendQuestionMailUseCase(
			questionSendingHistoryService,
			questionService,
			userService,
			emailService
		);
	}

	@Test
	@DisplayName("userId가 주어졌을 때 즉시 메일 발송 테스트")
	void testSendImmediatelyToUser() {
		// given
		Long questionId = 1L;
		Long userId = 10L;

		Question mockQuestion = new Question();
		ReflectionTestUtils.setField(mockQuestion, "id", questionId);
		ReflectionTestUtils.setField(mockQuestion, "title", "Test Title");

		User mockUser = new User("test@csalgo.com");
		ReflectionTestUtils.setField(mockUser, "id", userId);

		when(questionService.read(questionId)).thenReturn(mockQuestion);
		when(userService.read(userId)).thenReturn(mockUser);

		SendQuestionMailDto.Request request = SendQuestionMailDto.Request.builder()
			.questionId(questionId)
			.userId(userId)
			.build();

		// when
		useCase.execute(request);

		// then
		verify(emailService).send(eq("test@csalgo.com"), anyString(), anyString());
		verify(questionSendingHistoryService).create(eq(questionId), eq(userId));
	}

	@Test
	@DisplayName("userId 없이 전체 사용자에게 메일 발송 테스트")
	void testSendToAllUsers() {
		// given
		Long questionId = 3L;

		Question mockQuestion = new Question();
		ReflectionTestUtils.setField(mockQuestion, "id", questionId);
		ReflectionTestUtils.setField(mockQuestion, "title", "전체 발송 문제");

		User mockUser = new User("test@csalgo.com");
		ReflectionTestUtils.setField(mockUser, "id", 999L);

		when(questionService.read(questionId)).thenReturn(mockQuestion);
		when(userService.list()).thenReturn(List.of(mockUser));

		SendQuestionMailDto.Request request = SendQuestionMailDto.Request.builder()
			.questionId(questionId)
			.build();

		// when
		useCase.execute(request);

		// then
		verify(emailService).send(eq("test@csalgo.com"), anyString(), anyString());
		verify(questionSendingHistoryService).create(eq(questionId), eq(999L));
	}
}
