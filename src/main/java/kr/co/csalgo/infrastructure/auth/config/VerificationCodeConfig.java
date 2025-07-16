package kr.co.csalgo.infrastructure.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.infrastructure.auth.repository.RedisVerificationCodeRepository;

@Configuration
public class VerificationCodeConfig {

	@Value("${verification.code.expiration}")
	private long codeExpiration;

	@Bean
	public VerificationCodeRepository verificationCodeRepository(StringRedisTemplate redisTemplate) {
		return new RedisVerificationCodeRepository(
			redisTemplate,
			codeExpiration
		);
	}
}
