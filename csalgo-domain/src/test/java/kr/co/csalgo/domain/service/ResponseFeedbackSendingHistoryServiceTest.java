package kr.co.csalgo.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kr.co.csalgo.domain.question.entity.ResponseFeedback;
import kr.co.csalgo.domain.question.entity.ResponseFeedbackSendingHistory;
import kr.co.csalgo.domain.question.repository.ResponseFeedbackSendingHistoryRepository;
import kr.co.csalgo.domain.question.service.ResponseFeedbackSendingHistoryService;

@DisplayName("ResponseFeedbackSendingHistoryService Test")
class ResponseFeedbackSendingHistoryServiceTest {

	@Mock
	private ResponseFeedbackSendingHistoryRepository repository;

	@InjectMocks
	private ResponseFeedbackSendingHistoryService service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("발송 이력이 정상적으로 생성된다")
	void testCreateHistorySuccess() {
		ResponseFeedback feedback = mock(ResponseFeedback.class);
		ResponseFeedbackSendingHistory history = ResponseFeedbackSendingHistory.builder()
			.feedback(feedback)
			.build();

		when(repository.save(any())).thenReturn(history);

		service.create(feedback);

		verify(repository).save(any(ResponseFeedbackSendingHistory.class));
	}

	@Test
	@DisplayName("오늘 발송 이력이 존재하면 true를 반환한다")
	void testIsSentTrueSuccess() {
		ResponseFeedback feedback = mock(ResponseFeedback.class);
		LocalDate today = LocalDate.now();
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.atTime(LocalTime.MAX);

		when(repository.existsByResponseFeedbackAndCreatedAtBetween(feedback, start, end)).thenReturn(true);

		boolean result = service.isSent(feedback, today);

		assertThat(result).isTrue();
		verify(repository).existsByResponseFeedbackAndCreatedAtBetween(feedback, start, end);
	}

	@Test
	@DisplayName("오늘 발송 이력이 없으면 false를 반환한다")
	void testIsSentFalseSuccess() {
		ResponseFeedback feedback = mock(ResponseFeedback.class);
		LocalDate today = LocalDate.now();
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.atTime(LocalTime.MAX);

		when(repository.existsByResponseFeedbackAndCreatedAtBetween(feedback, start, end)).thenReturn(false);

		boolean result = service.isSent(feedback, today);

		assertThat(result).isFalse();
		verify(repository).existsByResponseFeedbackAndCreatedAtBetween(feedback, start, end);
	}
}
