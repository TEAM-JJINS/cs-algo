package kr.co.csalgo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.question.entity.Question;
import kr.co.csalgo.question.entity.QuestionSendingHistory;
import kr.co.csalgo.question.repository.QuestionSendingHistoryRepository;
import kr.co.csalgo.question.service.QuestionSendingHistoryService;
import kr.co.csalgo.question.service.QuestionService;
import kr.co.csalgo.user.entity.User;
import kr.co.csalgo.user.service.UserService;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionSendingHistoryService Test")
class QuestionSendingHistoryServiceTest {

	@Mock
	private QuestionSendingHistoryRepository questionSendingHistoryRepository;

	@Mock
	private QuestionService questionService;

	@Mock
	private UserService userService;

	private QuestionSendingHistoryService questionSendingHistoryService;

	@BeforeEach
	void setUp() {
		questionSendingHistoryService = new QuestionSendingHistoryService(
			questionSendingHistoryRepository,
			questionService,
			userService
		);
	}

	@Test
	@DisplayName("정상적으로 문제 전송 기록을 생성할 수 있다.")
	void testCreateHistorySuccess() {
		// given

		Question question = Question.builder()
			.title("What is TDD?")
			.build();

		User user = User.builder()
			.email("test@example.com")
			.build();

		QuestionSendingHistory history = QuestionSendingHistory.builder()
			.question(question)
			.user(user)
			.build();

		when(questionService.read(anyLong())).thenReturn(question);
		when(userService.read(anyLong())).thenReturn(user);
		when(questionSendingHistoryRepository.save(any())).thenReturn(history);

		// when
		QuestionSendingHistory result = questionSendingHistoryService.create(1L, 1L);

		// then
		assertNotNull(result);
		assertEquals(question, result.getQuestion());
		assertEquals(user, result.getUser());

		// verify save call
		ArgumentCaptor<QuestionSendingHistory> captor = ArgumentCaptor.forClass(QuestionSendingHistory.class);
		verify(questionSendingHistoryRepository).save(captor.capture());
		QuestionSendingHistory captured = captor.getValue();
		assertEquals(question, captured.getQuestion());
		assertEquals(user, captured.getUser());
	}

	@Test
	@DisplayName("사용자에게 보낸 문제 수를 가져온다.")
	void testCountByUser() {
		User user = User.builder()
			.email("test@csalgo.com")
			.build();
		long expectedCount = 5L;

		when(questionSendingHistoryRepository.countByUser(user)).thenReturn(expectedCount);

		long result = questionSendingHistoryService.count(user);

		assertEquals(expectedCount, result);
		verify(questionSendingHistoryRepository).countByUser(user);
	}

}
