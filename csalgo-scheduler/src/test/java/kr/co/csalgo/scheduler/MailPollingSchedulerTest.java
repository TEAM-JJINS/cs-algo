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

@DisplayName("MailPollingScheduler 테스트")
class MailPollingSchedulerTest {

	@Mock
	private JobLauncher jobLauncher;

	@Mock
	private Job feedbackJob;

	@InjectMocks
	private MailPollingScheduler mailPollingScheduler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("poll() 호출 시 feedbackJob이 실행되어야 한다")
	void testPoll_RunFeedbackJob() throws Exception {
		// given
		JobExecution jobExecution = mock(JobExecution.class);
		when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);

		// when
		mailPollingScheduler.poll();

		// then
		verify(jobLauncher, times(1)).run(eq(feedbackJob), any(JobParameters.class));
	}
}
