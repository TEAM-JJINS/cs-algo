package kr.co.csalgo.scheduler;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;

class BatchRetryExecutorTest {

	@Test
	@DisplayName("BatchRetryExecutor 실제 재시도 로직 검증")
	void testRealRetryExecutor() {
		RetryTemplate retryTemplate = RetryTemplate.builder()
			.maxAttempts(3)
			.fixedBackoff(10)
			.build();

		PlatformTransactionManager txManager = new ResourcelessTransactionManager();

		BatchRetryExecutor realExecutor = new BatchRetryExecutor(retryTemplate, txManager);
		AtomicInteger counter = new AtomicInteger();

		assertDoesNotThrow(() -> realExecutor.run(() -> {
			if (counter.incrementAndGet() == 1) {
				throw new RuntimeException("첫 번째 시도 실패");
			}
		}));

		assertThat(counter.get()).isEqualTo(2);
	}
}
