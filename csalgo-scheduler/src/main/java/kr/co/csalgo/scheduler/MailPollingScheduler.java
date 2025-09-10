package kr.co.csalgo.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailPollingScheduler {
	private final JobLauncher jobLauncher;

	@Qualifier("feedbackJob")
	private final Job feedbackJob;

	@Scheduled(cron = "0 */1 * * * *")
	@SchedulerLock(
		name = "mailPollingScheduler",
		lockAtMostFor = "3m",
		lockAtLeastFor = "1m"
	)
	public void poll() throws Exception {
		JobParameters params = new JobParametersBuilder()
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();
		jobLauncher.run(feedbackJob, params);
	}
}
