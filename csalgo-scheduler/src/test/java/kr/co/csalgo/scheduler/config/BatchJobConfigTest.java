package kr.co.csalgo.scheduler.config;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;

import kr.co.csalgo.CsAlgoSchedulerApplication;
import kr.co.csalgo.application.mail.usecase.RegisterQuestionResponseUseCase;
import kr.co.csalgo.application.mail.usecase.SendFeedbackMailUseCase;
import kr.co.csalgo.application.problem.usecase.SendDailyQuestionMailUseCase;
import kr.co.csalgo.scheduler.BatchRetryExecutor;

@SpringBatchTest
@ActiveProfiles("test")
@SpringBootTest(classes = CsAlgoSchedulerApplication.class)
class BatchJobConfigTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private Job dailyQuestionJob;

	@Autowired
	private Job feedbackJob;

	@MockitoBean
	private SendDailyQuestionMailUseCase sendDailyQuestionMailUseCase;
	@MockitoBean
	private RegisterQuestionResponseUseCase registerQuestionResponseUseCase;
	@MockitoBean
	private SendFeedbackMailUseCase sendFeedbackMailUseCase;
	@MockitoBean
	private BatchRetryExecutor batchRetryExecutor;

	@Test
	@DisplayName("dailyQuestionJob 실행 시 정상 완료된다")
	void testDailyQuestionJobRunsSuccessfully() throws Exception {
		JobExecution execution = jobLauncherTestUtils.getJobLauncher()
			.run(dailyQuestionJob,
				new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());

		assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}

	@Test
	@DisplayName("feedbackJob 실행 시 정상 완료된다")
	void testFeedbackJobRunsSuccessfully() throws Exception {
		JobExecution execution = jobLauncherTestUtils.getJobLauncher()
			.run(feedbackJob,
				new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());

		assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}

	@Test
	@DisplayName("BatchRetryExecutor 실제 재시도 로직 검증")
	void testRealRetryExecutor() {
		RetryTemplate retryTemplate = RetryTemplate.builder()
			.maxAttempts(3)
			.fixedBackoff(10)
			.build();

		PlatformTransactionManager txManager = new ResourcelessTransactionManager();

		BatchRetryExecutor realExecutor = new BatchRetryExecutor(retryTemplate, txManager);
		AtomicInteger counter = new AtomicInteger();

		assertDoesNotThrow(() -> realExecutor.run(() -> {
			if (counter.incrementAndGet() == 1) {
				throw new RuntimeException("첫 번째 시도 실패");
			}
		}));

		assertThat(counter.get()).isEqualTo(2);
	}

}
