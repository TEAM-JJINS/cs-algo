package kr.co.csalgo.infrastructure.auth.repository;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import redis.embedded.RedisServer;

@SpringBootTest
class RedisVerificationCodeRepositoryTest {

	private static RedisServer redisServer;

	@Autowired
	private StringRedisTemplate redisTemplate;

	private RedisVerificationCodeRepository repository;

	@BeforeAll
	static void startRedis() throws IOException {
		redisServer = new RedisServer(6380);
		redisServer.start();
	}

	@AfterAll
	static void stopRedis() throws IOException {
		if (redisServer != null && redisServer.isActive()) {
			redisServer.stop();
		}
	}

	@AfterEach
	void tearDown() {
		Set<String> keys = redisTemplate.keys("*");
		if (!keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
	}

	@Test
	@DisplayName("Redis에 인증코드를 저장하고 검증에 성공한다")
	void createAndVerify_success() {
		repository = new RedisVerificationCodeRepository(redisTemplate, 10);
		String email = "test@example.com";
		String code = "123456";

		repository.create(email, code, VerificationCodeType.SUBSCRIPTION);

		boolean result = repository.verify(email, code, VerificationCodeType.SUBSCRIPTION);

		assertThat(result).isTrue();
		assertThat(redisTemplate.opsForValue().get(VerificationCodeType.SUBSCRIPTION + "::" + email)).isNull();
	}

	@Test
	@DisplayName("잘못된 인증코드를 검증하면 실패하고 삭제되지 않는다")
	void verify_wrongCode() {
		repository = new RedisVerificationCodeRepository(redisTemplate, 10);
		String email = "test@example.com";
		repository.create(email, "123456", VerificationCodeType.SUBSCRIPTION);

		boolean result = repository.verify(email, "654321", VerificationCodeType.SUBSCRIPTION);

		assertThat(result).isFalse();
		assertThat(redisTemplate.opsForValue().get(VerificationCodeType.SUBSCRIPTION + "::" + email)).isEqualTo("123456");
	}
}
