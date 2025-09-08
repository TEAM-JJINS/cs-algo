package kr.co.csalgo.scheduler;

import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class BatchRetryExecutor {
	private final RetryTemplate retry;
	private final TransactionTemplate tx;

	public BatchRetryExecutor(RetryTemplate retryTemplate, PlatformTransactionManager txManager) {
		this.retry = retryTemplate;
		this.tx = new TransactionTemplate(txManager);
	}

	public void run(Runnable action) {
		retry.execute(
			r -> {
				tx.execute(status -> {
					action.run();
					return null;
				});
				return null;
			},
			r -> {
				throw new IllegalStateException("재시도 실패", r.getLastThrowable());
			}
		);
	}
}
