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
import kr.co.csalgo.application.admin.dto.AdminLoginDto;
import kr.co.csalgo.application.admin.dto.TokenPair;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.auth.entity.AuthCredential;
import kr.co.csalgo.domain.auth.port.RefreshTokenStore;
import kr.co.csalgo.domain.auth.port.TokenCrypto;
import kr.co.csalgo.domain.auth.service.AuthCredentialService;
import kr.co.csalgo.domain.auth.type.CredentialType;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;
import kr.co.csalgo.domain.user.type.Role;

class AdminLoginUseCaseTest {

	private UserService userService;
	private AuthCredentialService authCredentialService;
	private TokenCrypto tokenCrypto;
	private RefreshTokenStore refreshTokenStore;

	private AdminLoginUseCase useCase;

	@BeforeEach
	void setUp() {
		userService = mock(UserService.class);
		authCredentialService = mock(AuthCredentialService.class);
		tokenCrypto = mock(TokenCrypto.class);
		refreshTokenStore = mock(RefreshTokenStore.class);

		useCase = new AdminLoginUseCase(userService, authCredentialService, tokenCrypto, refreshTokenStore);
	}

	@Test
	@DisplayName("관리자 계정으로 로그인 성공")
	void adminLoginSuccess() {
		// given
		String email = "admin@test.com";
		String password = "1234";
		User user = User.builder()
			.email(email)
			.build();
		user.updateRole(Role.ADMIN);
		AuthCredential credential = mock(AuthCredential.class);

		when(userService.read(email)).thenReturn(user);
		when(authCredentialService.read(user.getId(), CredentialType.PASSWORD)).thenReturn(credential);
		when(credential.getPasswordHash()).thenReturn(password); // matches() 통과하도록 stub

		String refreshToken = "refresh-token";
		String accessToken = "access-token";

		// Jws<Claims> & Claims mocking
		Jws<Claims> parsed = mock(Jws.class);
		Claims claims = mock(Claims.class);
		String jti = UUID.randomUUID().toString();
		Date exp = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		when(claims.getId()).thenReturn(jti);
		when(claims.getExpiration()).thenReturn(exp);
		when(parsed.getPayload()).thenReturn(claims);

		when(tokenCrypto.createInitialRefreshToken(eq(email), anyString())).thenReturn(refreshToken);
		when(tokenCrypto.parse(refreshToken)).thenReturn(parsed);
		when(tokenCrypto.createAccessToken(eq(email), eq("ROLE_ADMIN"), anyString())).thenReturn(accessToken);

		AdminLoginDto.Request request = new AdminLoginDto.Request(email, password);

		// when
		TokenPair result = useCase.login(request);

		// then
		assertThat(result.getAccessToken()).isEqualTo(accessToken);
		assertThat(result.getRefreshToken()).isEqualTo(refreshToken);

		verify(refreshTokenStore).initFamily(anyString(), eq(jti), eq(exp.getTime()));
	}

	@Test
	@DisplayName("관리자가 아닌 계정 로그인 시 예외 발생")
	void throwIfNotAdmin() {
		// given
		String email = "user@test.com";
		User user = User.builder()
			.email(email)
			.build();

		when(userService.read(email)).thenReturn(user);

		AdminLoginDto.Request request = new AdminLoginDto.Request(email, "pw");

		// expect
		assertThatThrownBy(() -> useCase.login(request))
			.isInstanceOf(CustomBusinessException.class)
			.hasMessageContaining(ErrorCode.FORBIDDEN_ACCESS.getMessage());
	}

	@Test
	@DisplayName("비밀번호 불일치 시 예외 발생")
	void throwIfPasswordMismatch() {
		// given
		String email = "admin@test.com";
		User user = User.builder()
			.email(email)
			.build();
		user.updateRole(Role.ADMIN); // 관리자 역할로 설정
		AuthCredential credential = mock(AuthCredential.class);

		when(userService.read(email)).thenReturn(user);
		when(authCredentialService.read(user.getId(), CredentialType.PASSWORD)).thenReturn(credential);
		when(credential.getPasswordHash()).thenReturn("encodedPassword"); // 불일치

		AdminLoginDto.Request request = new AdminLoginDto.Request(email, "wrong");

		// expect
		assertThatThrownBy(() -> useCase.login(request))
			.isInstanceOf(CustomBusinessException.class)
			.hasMessageContaining(ErrorCode.CREDENTIAL_NOT_FOUND.getMessage());
	}
}
