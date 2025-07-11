package kr.co.csalgo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "3m")
public class ShedLockConfig {
	@Bean
	public LockProvider lockProvider(RedisConnectionFactory redisConnectionFactory) {
		return new RedisLockProvider(redisConnectionFactory);
	}
}
