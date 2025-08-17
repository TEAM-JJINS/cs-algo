package kr.co.csalgo.infrastructure.config;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.sentry.Hint;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class SentryConfigTest {

	@Test
	@DisplayName("INFO 레벨은 Sentry 전송에서 제외된다")
	void testSentryBeforeSendSuccess() {
		SentryOptions options = new SentryOptions();
		new SentryConfig().sentryOptions().configure(options);

		SentryEvent infoEvent = new SentryEvent();
		infoEvent.setLevel(SentryLevel.INFO);

		SentryEvent exceptionEvent = new SentryEvent(new CustomBusinessException(ErrorCode.INTERNAL_SERVER_ERROR));

		Hint hint = new Hint();
		SentryEvent filteredInfo = options.getBeforeSend().execute(infoEvent, hint);
		SentryEvent filteredBizEx = options.getBeforeSend().execute(exceptionEvent, hint);

		assertThat(filteredInfo).isNull();
		assertThat(filteredBizEx).isNull();
	}

}
