package kr.co.csalgo.presentation.scheduler;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kr.co.csalgo.application.problem.usecase.SendDailyQuestionMailUseCase;

@DisplayName("QuestionScheduler 테스트")
class QuestionSchedulerTest {

	@Mock
	private SendDailyQuestionMailUseCase useCase;
	@InjectMocks
	private QuestionScheduler scheduler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		scheduler = new QuestionScheduler(useCase);
	}

	@Test
	@DisplayName("매일 8시에 execute가 실행된다.")
	void testScheduler_Run_Success() {
		scheduler.run();
		verify(useCase, times(1)).execute();
	}
}
