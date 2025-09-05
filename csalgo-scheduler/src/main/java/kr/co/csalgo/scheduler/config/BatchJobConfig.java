package kr.co.csalgo.scheduler.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchJobConfig {

	private final JobExecutionListener jobExecutionListener;
	private final StepExecutionListener stepExecutionListener;

	@Bean
	public Job dailyProblemJob(JobRepository jobRepository, PlatformTransactionManager tx) {
		return new JobBuilder("dailyProblemJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(jobExecutionListener)
			.start(problemTaskletStep(jobRepository, tx))
			.next(problemChunkStep(jobRepository, tx))
			.build();
	}

	@Bean
	public Job feedbackJob(JobRepository jobRepository, PlatformTransactionManager tx) {
		return new JobBuilder("feedbackJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(jobExecutionListener)
			.start(feedbackTaskletStep(jobRepository, tx))
			.next(feedbackChunkStep(jobRepository, tx))
			.build();
	}
}
