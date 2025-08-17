package kr.co.csalgo.infrastructure.security;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BCryptPasswordHasher 단위 테스트")
class BCryptPasswordHasherTest {

	private final BCryptPasswordHasher hasher = new BCryptPasswordHasher();

	@Test
	@DisplayName("encode는 bcrypt 해시를 반환하며 raw와 다르다")
	void encode_returnsBcryptHash_andNotEqualToRaw() {
		String raw = "P@ssw0rd!";
		String encoded = hasher.encode(raw);

		assertThat(encoded).isNotNull();
		assertThat(encoded).startsWith("$2");
		assertThat(encoded).isNotEqualTo(raw);
		assertThat(encoded.length()).isGreaterThanOrEqualTo(60);
	}

	@Test
	@DisplayName("matches는 raw와 encoded가 일치할 때 true를 반환한다")
	void matches_returnsTrue_whenRawMatchesEncoded() {
		String raw = "my-secret";
		String encoded = hasher.encode(raw);

		assertThat(hasher.matches(raw, encoded)).isTrue();
	}

	@Test
	@DisplayName("matches는 raw와 encoded가 다르면 false를 반환한다")
	void matches_returnsFalse_whenRawDoesNotMatchEncoded() {
		String raw = "my-secret";
		String encoded = hasher.encode(raw);

		assertThat(hasher.matches("other-secret", encoded)).isFalse();
	}

	@Test
	@DisplayName("같은 raw를 여러 번 encode하면 서로 다른 해시가 생성된다 (랜덤 솔트)")
	void encode_sameRawMultipleTimes_producesDifferentHashes_dueToRandomSalt() {
		String raw = "constant";

		String encoded1 = hasher.encode(raw);
		String encoded2 = hasher.encode(raw);

		assertThat(encoded1).isNotEqualTo(encoded2); // different salts
		assertThat(hasher.matches(raw, encoded1)).isTrue();
		assertThat(hasher.matches(raw, encoded2)).isTrue();
	}
}
