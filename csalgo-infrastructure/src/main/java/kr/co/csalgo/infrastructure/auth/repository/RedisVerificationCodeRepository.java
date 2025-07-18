package kr.co.csalgo.infrastructure.auth.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;

import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisVerificationCodeRepository implements VerificationCodeRepository {
	private final StringRedisTemplate redisTemplate;
	private final long expireTime;

	@Override
	public void create(String email, String code, VerificationCodeType verificationCodeType) {
		String key = generateKey(verificationCodeType, email);
		redisTemplate.opsForValue().set(key, code, expireTime, TimeUnit.SECONDS);
	}

	@Override
	public boolean verify(String email, String code, VerificationCodeType verificationCodeType) {
		String key = generateKey(verificationCodeType, email);
		String storedCode = redisTemplate.opsForValue().get(key);

		if (storedCode != null && storedCode.equals(code)) {
			redisTemplate.delete(key);
			return true;
		}
		return false;
	}
}
