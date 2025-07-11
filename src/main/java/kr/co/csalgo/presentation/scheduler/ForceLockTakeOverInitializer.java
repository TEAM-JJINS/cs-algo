package kr.co.csalgo.presentation.scheduler;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Profile("dev")
public class ForceLockTakeOverInitializer {
	private final StringRedisTemplate redisTemplate;

	private static final String LOCK_KEY = "job-lock:default:mailPollingScheduler";

	@PostConstruct
	public void forceLockTakeOver() {
		redisTemplate.delete(LOCK_KEY);
	}

	@PreDestroy
	public void releaseLock() {
		redisTemplate.delete(LOCK_KEY);
	}
}
