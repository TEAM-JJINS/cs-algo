package kr.co.csalgo.application.admin.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.csalgo.application.admin.dto.AdminRefreshDto;
import kr.co.csalgo.application.admin.dto.TokenPair;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.auth.port.RefreshTokenStore;
import kr.co.csalgo.domain.auth.port.TokenCrypto;
import kr.co.csalgo.domain.user.type.Role;

class AdminRefreshTokenUseCaseTest {

	private TokenCrypto tokenCrypto;
	private RefreshTokenStore refreshTokenStore;
	private AdminRefreshTokenUseCase useCase;

	@BeforeEach
	void setUp() {
		tokenCrypto = mock(TokenCrypto.class);
		refreshTokenStore = mock(RefreshTokenStore.class);
		useCase = new AdminRefreshTokenUseCase(tokenCrypto, refreshTokenStore);
	}

	@Test
	@DisplayName("리프레시 토큰 갱신 성공")
	void refreshTokenSuccess() {
		// given
		String oldRefreshToken = "old-refresh-token";
		String newRefreshToken = "new-refresh-token";
		String familyId = "family-123";
		String oldJti = UUID.randomUUID().toString();
		String newJti = UUID.randomUUID().toString();
		String subject = "admin@test.com";

		// 기존 refreshToken Claims mocking
		Jws<Claims> parsed = mock(Jws.class);
		Claims oldClaims = mock(Claims.class);
		when(parsed.getPayload()).thenReturn(oldClaims);
		when(oldClaims.get("typ", String.class)).thenReturn("refresh");
		when(oldClaims.get("family", String.class)).thenReturn(familyId);
		when(oldClaims.getId()).thenReturn(oldJti);
		when(oldClaims.getSubject()).thenReturn(subject);

		// 새로운 refreshToken Claims mocking
		Jws<Claims> newParsed = mock(Jws.class);
		Claims newClaims = mock(Claims.class);
		when(newParsed.getPayload()).thenReturn(newClaims);
		when(newClaims.getId()).thenReturn(newJti);
		when(newClaims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 1000 * 60 * 60));

		when(tokenCrypto.parse(oldRefreshToken)).thenReturn(parsed);
		when(tokenCrypto.createRotatedRefreshToken(subject, familyId)).thenReturn(newRefreshToken);
		when(tokenCrypto.parse(newRefreshToken)).thenReturn(newParsed);
		when(refreshTokenStore.isFamilyRevoked(familyId)).thenReturn(false);
		when(refreshTokenStore.rotateIfLatest(eq(familyId), eq(oldJti), eq(newJti), anyLong())).thenReturn(true);
		when(tokenCrypto.createAccessToken(subject, Role.ADMIN.name(), familyId)).thenReturn("new-access-token");

		// when
		TokenPair result = useCase.refresh(AdminRefreshDto.Request.builder().refreshToken(oldRefreshToken).build());

		// then
		assertThat(result.getAccessToken()).isEqualTo("new-access-token");
		assertThat(result.getRefreshToken()).isEqualTo(newRefreshToken);

		verify(refreshTokenStore).rotateIfLatest(eq(familyId), eq(oldJti), eq(newJti), anyLong());
	}

	@Test
	@DisplayName("잘못된 토큰 타입이면 예외 발생")
	void throwIfInvalidTokenType() {
		String refreshToken = "bad-token";

		Jws<Claims> parsed = mock(Jws.class);
		Claims claims = mock(Claims.class);
		when(parsed.getPayload()).thenReturn(claims);
		when(claims.get("typ", String.class)).thenReturn("access"); // refresh 아님
		when(tokenCrypto.parse(refreshToken)).thenReturn(parsed);

		assertThatThrownBy(() -> useCase.refresh(AdminRefreshDto.Request.builder().refreshToken(refreshToken).build()))
			.isInstanceOf(CustomBusinessException.class)
			.hasMessageContaining(ErrorCode.INVALID_TOKEN_TYPE.getMessage());
	}

	@Test
	@DisplayName("패밀리 전체가 취소되었으면 예외 발생")
	void throwIfFamilyRevoked() {
		String refreshToken = "revoked-token";
		String familyId = "family-123";

		Jws<Claims> parsed = mock(Jws.class);
		Claims claims = mock(Claims.class);
		when(parsed.getPayload()).thenReturn(claims);
		when(claims.get("typ", String.class)).thenReturn("refresh");
		when(claims.get("family", String.class)).thenReturn(familyId);
		when(claims.getId()).thenReturn("jti");
		when(tokenCrypto.parse(refreshToken)).thenReturn(parsed);
		when(refreshTokenStore.isFamilyRevoked(familyId)).thenReturn(true);

		assertThatThrownBy(() -> useCase.refresh(AdminRefreshDto.Request.builder().refreshToken(refreshToken).build()))
			.isInstanceOf(CustomBusinessException.class)
			.hasMessageContaining(ErrorCode.REFRESH_FAMILY_REVOKED.getMessage());
	}

	@Test
	@DisplayName("리프레시 토큰 재사용 탐지 시 예외 발생")
	void throwIfTokenReused() {
		String refreshToken = "reuse-token";
		String familyId = "family-123";
		String oldJti = "old-jti";
		String newJti = "new-jti";
		String subject = "admin@test.com";

		// 기존 refreshToken
		Jws<Claims> parsed = mock(Jws.class);
		Claims oldClaims = mock(Claims.class);
		when(parsed.getPayload()).thenReturn(oldClaims);
		when(oldClaims.get("typ", String.class)).thenReturn("refresh");
		when(oldClaims.get("family", String.class)).thenReturn(familyId);
		when(oldClaims.getId()).thenReturn(oldJti);
		when(oldClaims.getSubject()).thenReturn(subject);
		when(tokenCrypto.parse(refreshToken)).thenReturn(parsed);

		// 새로운 refreshToken
		Jws<Claims> newParsed = mock(Jws.class);
		Claims newClaims = mock(Claims.class);
		when(newParsed.getPayload()).thenReturn(newClaims);
		when(newClaims.getId()).thenReturn(newJti);
		when(newClaims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 1000 * 60 * 60));

		when(tokenCrypto.createRotatedRefreshToken(subject, familyId)).thenReturn("new-refresh-token");
		when(tokenCrypto.parse("new-refresh-token")).thenReturn(newParsed);
		when(refreshTokenStore.isFamilyRevoked(familyId)).thenReturn(false);
		when(refreshTokenStore.rotateIfLatest(eq(familyId), eq(oldJti), eq(newJti), anyLong())).thenReturn(false);

		assertThatThrownBy(() -> useCase.refresh(AdminRefreshDto.Request.builder().refreshToken(refreshToken).build()))
			.isInstanceOf(CustomBusinessException.class)
			.hasMessageContaining(ErrorCode.REFRESH_TOKEN_REUSE.getMessage());

		verify(refreshTokenStore).revokeFamily(familyId);
	}
}
