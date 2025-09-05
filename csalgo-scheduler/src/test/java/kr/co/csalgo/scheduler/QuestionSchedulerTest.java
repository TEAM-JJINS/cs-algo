package kr.co.csalgo.scheduler;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

@DisplayName("QuestionScheduler 테스트")
class QuestionSchedulerTest {

	@Mock
	private JobLauncher jobLauncher;

	@Mock
	private Job dailyQuestionJob;

	@InjectMocks
	private QuestionScheduler scheduler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("매일 8시에 dailyProblemJob이 실행된다.")
	void testScheduler_Run_Success() throws Exception {
		// given
		JobExecution jobExecution = mock(JobExecution.class);
		when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);

		// when
		scheduler.run();

		// then
		verify(jobLauncher, times(1)).run(eq(dailyQuestionJob), any(JobParameters.class));
	}
}
