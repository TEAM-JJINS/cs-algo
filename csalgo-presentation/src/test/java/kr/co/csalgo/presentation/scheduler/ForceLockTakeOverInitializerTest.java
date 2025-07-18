package kr.co.csalgo.presentation.scheduler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.StringRedisTemplate;

class ForceLockTakeOverInitializerTest {

	@Test
	void postConstructShouldCallDelete() {
		StringRedisTemplate redisTemplate = Mockito.mock(StringRedisTemplate.class);
		ForceLockTakeOverInitializer initializer = new ForceLockTakeOverInitializer(redisTemplate);

		initializer.forceLockTakeOver();
		initializer.releaseLock();

		Mockito.verify(redisTemplate, Mockito.times(2)).delete("job-lock:default:mailPollingScheduler");
	}
}

