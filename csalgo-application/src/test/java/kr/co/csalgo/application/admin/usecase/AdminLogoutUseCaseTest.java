package kr.co.csalgo.application.admin.usecase;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.csalgo.application.admin.dto.AdminRefreshDto;
import kr.co.csalgo.common.message.CommonResponse;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.auth.port.RefreshTokenStore;
import kr.co.csalgo.domain.auth.port.TokenCrypto;

public class AdminLogoutUseCaseTest {
	private TokenCrypto tokenCrypto;
	private RefreshTokenStore refreshTokenStore;
	private AdminLogoutUseCase logoutUseCase;

	@BeforeEach
	void setUp() {
		tokenCrypto = mock(TokenCrypto.class);
		refreshTokenStore = mock(RefreshTokenStore.class);
		logoutUseCase = new AdminLogoutUseCase(tokenCrypto, refreshTokenStore);
	}

	@Test
	@DisplayName("관리자 로그아웃 성공 ")
	void testLogoutSuccess() {
		String refreshToken = "dummy.jwt.token";
		AdminRefreshDto.Request request = new AdminRefreshDto.Request(refreshToken);

		Jws<Claims> jws = mock(Jws.class);
		Claims claims = mock(Claims.class);

		when(tokenCrypto.parse(refreshToken)).thenReturn(jws);
		when(jws.getPayload()).thenReturn(claims);
		when(claims.get("family", String.class)).thenReturn("family-123");

		CommonResponse response = logoutUseCase.logout(request);

		verify(refreshTokenStore, times(1)).revokeFamily("family-123");
		assertThat(response.getMessage()).isEqualTo(MessageCode.LOGOUT_SUCCESS.getMessage());
	}
}
