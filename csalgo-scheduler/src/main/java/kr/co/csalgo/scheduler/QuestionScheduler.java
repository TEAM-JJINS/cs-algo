package kr.co.csalgo.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionScheduler {

	private final JobLauncher jobLauncher;
	private final Job dailyQuestionJob;

	@Scheduled(cron = "0 0 8 * * MON-FRI", zone = "Asia/Seoul")
	public void run() throws Exception {
		JobParameters params = new JobParametersBuilder()
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();
		jobLauncher.run(dailyQuestionJob, params);
	}
}

