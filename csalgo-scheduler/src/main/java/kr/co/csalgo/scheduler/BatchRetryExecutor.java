package kr.co.csalgo.scheduler;

import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			context -> {
				int attempt = context.getRetryCount() + 1;
				try {
					tx.execute(status -> {
						action.run();
						return null;
					});
					log.info("[재시도 성공] {}번째 시도", attempt);
				} catch (Exception e) {
					log.warn("[재시도 실패] {}번째 시도 - {}", attempt, e.getMessage());
					throw e;
				}
				return null;
			},
			context -> {
				int totalAttempts = context.getRetryCount();
				Throwable lastThrowable = context.getLastThrowable();
				log.error("[Job 최종 실패] 총 {}회 시도 후 종료 - {}",
					totalAttempts,
					lastThrowable != null ? lastThrowable.getMessage() : "unknown");
				throw new CustomBusinessException(ErrorCode.BATCH_RETRY_ERROR);
			}
		);

		log.info("[Job 최종 성공]");
	}
}
