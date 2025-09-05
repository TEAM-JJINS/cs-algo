package kr.co.csalgo.scheduler;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Profile("dev")
@Slf4j
public class ForceLockTakeOverInitializer {
	private final StringRedisTemplate redisTemplate;

	private static final String LOCK_KEY = "job-lock:default:mailPollingScheduler";

	@PostConstruct
	public void forceLockTakeOver() {
		try {
			redisTemplate.delete(LOCK_KEY);
			log.info("강제 락 인수 완료: {}", LOCK_KEY);
		} catch (Exception e) {
			log.warn("강제 락 인수 실패: {}", LOCK_KEY, e);
		}
	}

	@PreDestroy
	public void releaseLock() {
		try {
			redisTemplate.delete(LOCK_KEY);
			log.info("락 해제 완료: {}", LOCK_KEY);
		} catch (Exception e) {
			log.warn("락 해제 실패: {}", LOCK_KEY, e);
		}
	}
}
