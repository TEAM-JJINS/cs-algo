package kr.co.csalgo.scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {
	@Bean
	public RetryTemplate retryTemplate() {
		ExponentialBackOffPolicy backoff = new ExponentialBackOffPolicy();
		backoff.setInitialInterval(2000);
		backoff.setMultiplier(2.0);
		backoff.setMaxInterval(15000);

		RetryTemplate retry = new RetryTemplate();
		retry.setBackOffPolicy(backoff);
		return retry;
	}
}
