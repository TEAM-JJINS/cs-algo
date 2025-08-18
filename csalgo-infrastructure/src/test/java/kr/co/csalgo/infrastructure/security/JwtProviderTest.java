package kr.co.csalgo.infrastructure.security;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@DisplayName("JwtProvider 단위 테스트")
class JwtProviderTest {

	private JwtProvider jwtProvider;

	@BeforeEach
	void setUp() {
		// 32바이트 이상 시크릿 필요 (HMAC-SHA256)
		String secret = "01234567890123456789012345678901";
		jwtProvider = new JwtProvider(secret, 1000L * 60 * 10, 1000L * 60 * 60 * 24);
	}

	@Test
	@DisplayName("AccessToken 생성 시 subject, role, family, typ(access) 클레임이 포함된다")
	void createAccessToken_containsSubjectRoleAndFamily() {
		String token = jwtProvider.createAccessToken("user1", "USER", "fam123");

		Jws<Claims> parsed = jwtProvider.parse(token);
		Claims claims = parsed.getPayload();

		assertThat(claims.getSubject()).isEqualTo("user1");
		assertThat(claims.get("role", String.class)).isEqualTo("USER");
		assertThat(claims.get("typ", String.class)).isEqualTo("access");
		assertThat(claims.get("family", String.class)).isEqualTo("fam123");
		assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
	}

	@Test
	@DisplayName("AccessToken 생성 시 familyId가 없으면 family 클레임은 null이다")
	void createAccessToken_withoutFamilyId_allowsNullFamilyClaim() {
		String token = jwtProvider.createAccessToken("user2", "ADMIN");

		Jws<Claims> parsed = jwtProvider.parse(token);
		Claims claims = parsed.getPayload();

		assertThat(claims.getSubject()).isEqualTo("user2");
		assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
		assertThat(claims.get("family")).isNull();
	}

	@Test
	@DisplayName("초기 RefreshToken 생성 시 typ(refresh), family, jti 클레임이 포함된다")
	void createInitialRefreshToken_containsTypRefreshAndFamilyAndJti() {
		String token = jwtProvider.createInitialRefreshToken("user3", "fam456");

		Jws<Claims> parsed = jwtProvider.parse(token);
		Claims claims = parsed.getPayload();

		assertThat(claims.getSubject()).isEqualTo("user3");
		assertThat(claims.get("typ", String.class)).isEqualTo("refresh");
		assertThat(claims.get("family", String.class)).isEqualTo("fam456");
		assertThat(claims.getId()).isNotBlank();
	}

	@Test
	@DisplayName("회전 RefreshToken은 매번 다른 jti 값을 가진다")
	void createRotatedRefreshToken_producesDifferentJtiEachTime() {
		String token1 = jwtProvider.createRotatedRefreshToken("user4", "fam789");
		String token2 = jwtProvider.createRotatedRefreshToken("user4", "fam789");

		String jti1 = jwtProvider.parse(token1).getPayload().getId();
		String jti2 = jwtProvider.parse(token2).getPayload().getId();

		assertThat(jti1).isNotEqualTo(jti2);
	}

	@Test
	@DisplayName("잘못된 토큰을 파싱하면 예외가 발생한다")
	void parse_invalidToken_throwsException() {
		String invalidToken = "invalid.jwt.token";

		assertThatThrownBy(() -> jwtProvider.parse(invalidToken))
			.isInstanceOf(Exception.class);
	}
}
