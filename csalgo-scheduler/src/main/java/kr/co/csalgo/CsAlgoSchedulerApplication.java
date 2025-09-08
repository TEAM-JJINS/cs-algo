package kr.co.csalgo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableBatchProcessing
public class CsAlgoSchedulerApplication {
	public static void main(String[] args) {
		SpringApplication.run(CsAlgoSchedulerApplication.class, args);
	}
}

