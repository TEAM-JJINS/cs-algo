package kr.co.csalgo.infrastructure.auth.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@DisplayName("RedisRefreshTokenRepository 단위 테스트")
class RedisRefreshTokenRepositoryTest {

	private static LettuceConnectionFactory connectionFactory;
	private static StringRedisTemplate redisTemplate;
	private RedisRefreshTokenRepository repository;

	@BeforeAll
	static void setUpRedis() {
		// 실제 테스트 환경에서는 Embedded Redis 또는 Testcontainers Redis를 사용할 수 있음
		connectionFactory = new LettuceConnectionFactory("localhost", 6379);
		connectionFactory.afterPropertiesSet();
		redisTemplate = new StringRedisTemplate(connectionFactory);
	}

	@BeforeEach
	void setUp() {
		repository = new RedisRefreshTokenRepository(redisTemplate, 10000L);
		redisTemplate.getConnectionFactory().getConnection().flushAll();
	}

	@AfterAll
	static void tearDown() {
		if (connectionFactory != null) {
			connectionFactory.destroy();
		}
	}

	@Test
	@DisplayName("initFamily는 current JTI를 저장하고 revoked 키를 제거한다")
	void initFamily_storesCurrentAndClearsRevoked() {
		repository.initFamily("fam1", "jti-123", System.currentTimeMillis() + 5000);

		String cur = redisTemplate.opsForValue().get("rt:family:fam1:current");
		String revoked = redisTemplate.opsForValue().get("rt:family:fam1:revoked");

		assertThat(cur).isEqualTo("jti-123");
		assertThat(revoked).isNull();
	}

	@Test
	@DisplayName("rotateIfLatest는 현재 JTI와 일치하면 교체하고 true를 반환한다")
	void rotateIfLatest_success() {
		String familyId = "fam2";
		repository.initFamily(familyId, "jti-old", System.currentTimeMillis() + 5000);

		boolean ok = repository.rotateIfLatest(familyId, "jti-old", "jti-new", System.currentTimeMillis() + 5000);

		assertThat(ok).isTrue();
		String cur = redisTemplate.opsForValue().get("rt:family:" + familyId + ":current");
		assertThat(cur).isEqualTo("jti-new");
	}

	@Test
	@DisplayName("rotateIfLatest는 현재 JTI와 다르면 false를 반환한다")
	void rotateIfLatest_fail_dueToMismatch() {
		String familyId = "fam3";
		repository.initFamily(familyId, "jti-old", System.currentTimeMillis() + 5000);

		boolean ok = repository.rotateIfLatest(familyId, "wrong-jti", "jti-new", System.currentTimeMillis() + 5000);

		assertThat(ok).isFalse();
		String cur = redisTemplate.opsForValue().get("rt:family:" + familyId + ":current");
		assertThat(cur).isEqualTo("jti-old");
	}

	@Test
	@DisplayName("revokeFamily는 revoked 키를 생성하고 current 키를 제거한다")
	void revokeFamily_marksRevokedAndDeletesCurrent() {
		String familyId = "fam4";
		repository.initFamily(familyId, "jti-xyz", System.currentTimeMillis() + 5000);

		repository.revokeFamily(familyId);

		Boolean revokedExists = redisTemplate.hasKey("rt:family:" + familyId + ":revoked");
		Boolean currentExists = redisTemplate.hasKey("rt:family:" + familyId + ":current");

		assertThat(revokedExists).isTrue();
		assertThat(currentExists).isFalse();
	}

	@Test
	@DisplayName("isFamilyRevoked는 revoked 키 존재 여부를 반환한다")
	void isFamilyRevoked_checksKeyExistence() {
		String familyId = "fam5";
		assertThat(repository.isFamilyRevoked(familyId)).isFalse();

		repository.revokeFamily(familyId);
		assertThat(repository.isFamilyRevoked(familyId)).isTrue();
	}
}
