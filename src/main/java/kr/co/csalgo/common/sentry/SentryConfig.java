package kr.co.csalgo.common.sentry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.sentry.Sentry;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import kr.co.csalgo.common.exception.CustomBusinessException;

@Configuration
public class SentryConfig {

	@Bean
	public Sentry.OptionsConfiguration<SentryOptions> sentryOptions() {
		return options -> {
			options.setBeforeSend((event, hint) -> {
				Throwable throwable = event.getThrowable();

				if (throwable instanceof CustomBusinessException) {
					return null;
				}

				if (event.getLevel() != null && event.getLevel().ordinal() <= SentryLevel.INFO.ordinal()) {
					return null;
				}

				return event;
			});
		};
	}
}
