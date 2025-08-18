package kr.co.csalgo.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.sentry.Sentry;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;

@Configuration
public class SentryConfig {

	@Bean
	public Sentry.OptionsConfiguration<SentryOptions> sentryOptions() {
		return options -> {
			options.setBeforeSend((event, hint) -> {
				Throwable throwable = event.getThrowable();

				if (throwable instanceof CustomBusinessException) {
					if (((CustomBusinessException)throwable).getErrorCode() == ErrorCode.REFRESH_TOKEN_REUSE) {
						return event;
					}
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
