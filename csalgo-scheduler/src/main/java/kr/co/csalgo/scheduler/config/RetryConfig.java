package kr.co.csalgo.scheduler.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

	private static final int RETRY_COUNT = 3;
	private static final long BACK_OFF_DELAY = 1000L;

	@Bean
	public RetryTemplate retryTemplate() {
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(RETRY_COUNT);

		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(BACK_OFF_DELAY);

		List<Class<? extends Throwable>> retryOn = List.of(RuntimeException.class);

		return RetryTemplate.builder()
			.customPolicy(retryPolicy)
			.customBackoff(backOffPolicy)
			.retryOn(retryOn)
			.build();
	}
}

