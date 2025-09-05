package kr.co.csalgo.scheduler.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import kr.co.csalgo.application.mail.usecase.RegisterQuestionResponseUseCase;
import kr.co.csalgo.application.mail.usecase.SendFeedbackMailUseCase;
import kr.co.csalgo.application.problem.usecase.SendDailyQuestionMailUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchJobConfig {

	private final JobExecutionListener jobExecutionListener;
	private final StepExecutionListener stepExecutionListener;

	private final SendDailyQuestionMailUseCase sendDailyQuestionMailUseCase;
	private final RegisterQuestionResponseUseCase registerQuestionResponseUseCase;
	private final SendFeedbackMailUseCase sendFeedbackMailUseCase;

	@Bean
	public Job dailyQuestionJob(JobRepository jobRepository, PlatformTransactionManager tx) {
		return new JobBuilder("dailyQuestionJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(jobExecutionListener)
			.start(questionTaskletStep(jobRepository, tx))
			.build();
	}

	@Bean
	@JobScope
	public Step questionTaskletStep(JobRepository jobRepository, PlatformTransactionManager tx) {
		return new StepBuilder("questionTaskletStep", jobRepository)
			.listener(stepExecutionListener)
			.tasklet((contribution, chunkContext) -> {
				sendDailyQuestionMailUseCase.execute();
				return RepeatStatus.FINISHED;
			}, tx)
			.build();
	}

	@Bean
	public Job feedbackJob(JobRepository jobRepository, PlatformTransactionManager tx) {
		return new JobBuilder("feedbackJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(jobExecutionListener)
			.start(feedbackTaskletStep(jobRepository, tx))
			.build();
	}

	@Bean
	@JobScope
	public Step feedbackTaskletStep(JobRepository jobRepository, PlatformTransactionManager tx) {
		return new StepBuilder("feedbackTaskletStep", jobRepository)
			.listener(stepExecutionListener)
			.tasklet((contribution, chunkContext) -> {
				registerQuestionResponseUseCase.execute();
				sendFeedbackMailUseCase.execute();
				return RepeatStatus.FINISHED;
			}, tx)
			.build();
	}
}
