package kr.co.csalgo.infrastructure.auth.repository;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import kr.co.csalgo.domain.auth.port.RefreshTokenStore;
import lombok.NonNull;

@Component
public class RedisRefreshTokenRepository implements RefreshTokenStore {
	private final StringRedisTemplate redis;
	private final long defaultTtlMs;

	public RedisRefreshTokenRepository(StringRedisTemplate redis,
		@Value("${jwt.refresh-ttl-ms:2592000000}") long refreshTtlMs) {
		this.redis = redis;
		this.defaultTtlMs = refreshTtlMs;
	}

	private static String currentKey(String familyId) {
		return "rt:family:" + familyId + ":current";
	}

	private static String revokedKey(String familyId) {
		return "rt:family:" + familyId + ":revoked";
	}

	@Override
	public void initFamily(@NonNull String familyId, @NonNull String currentJti, long expMs) {
		long ttlMs = ttl(expMs);
		redis.opsForValue().set(currentKey(familyId), currentJti, Duration.ofMillis(ttlMs));
		redis.delete(revokedKey(familyId));
	}

	@Override
	public boolean rotateIfLatest(@NonNull String familyId, @NonNull String presentedJti, @NonNull String newJti, long expMs) {
		if (redis.hasKey(revokedKey(familyId))) {
			return false;
		}

		long ttlMs = ttl(expMs);
		String script = "local cur = redis.call('GET', KEYS[1]) "
			+ "if (cur == ARGV[1]) then "
			+ "  redis.call('SET', KEYS[1], ARGV[2], 'PX', ARGV[3]) "
			+ "  return 1 else return 0 end";
		DefaultRedisScript<Long> cas = new DefaultRedisScript<>(script, Long.class);
		try {
			Long ok = redis.execute(cas, List.of(currentKey(familyId)), presentedJti, newJti, String.valueOf(ttlMs));
			return ok == 1L;
		} catch (DataAccessException e) {
			return false;
		}
	}

	@Override
	public void revokeFamily(@NonNull String familyId) {
		redis.opsForValue().set(revokedKey(familyId), "1", Duration.ofMillis(defaultTtlMs));
		redis.delete(currentKey(familyId));
	}

	@Override
	public boolean isFamilyRevoked(@NonNull String familyId) {
		return redis.hasKey(revokedKey(familyId));
	}

	private long ttl(long expMs) {
		if (expMs <= 0) {
			return defaultTtlMs;
		}
		long remain = expMs - System.currentTimeMillis();
		return remain > 0 ? remain : 1000L;
	}
}
