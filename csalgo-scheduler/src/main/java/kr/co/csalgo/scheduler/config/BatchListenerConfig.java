package kr.co.csalgo.scheduler.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BatchListenerConfig {

	@Bean
	public JobExecutionListener jobExecutionListener() {
		return new JobExecutionListenerSupport() {
			@Override
			public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
				log.info("[Job 시작] {}", jobExecution.getJobInstance().getJobName());
			}

			@Override
			public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
				log.info("[Job 종료] {}", jobExecution.getJobInstance().getJobName());
			}
		};
	}

	@Bean
	public StepExecutionListener stepExecutionListener() {
		return new StepExecutionListenerSupport() {
			@Override
			public void beforeStep(org.springframework.batch.core.StepExecution stepExecution) {
				log.info("[Step 시작] {}", stepExecution.getStepName());
			}

			@Override
			public ExitStatus afterStep(org.springframework.batch.core.StepExecution stepExecution) {
				log.info("[Step 종료] {}", stepExecution.getStepName());
				return stepExecution.getExitStatus();
			}
		};
	}
}
