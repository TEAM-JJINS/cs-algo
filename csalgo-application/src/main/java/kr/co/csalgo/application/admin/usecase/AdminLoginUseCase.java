package kr.co.csalgo.application.admin.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.csalgo.application.admin.dto.AdminLoginDto;
import kr.co.csalgo.application.admin.dto.TokenPair;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.auth.entity.AuthCredential;
import kr.co.csalgo.domain.auth.port.PasswordHasher;
import kr.co.csalgo.domain.auth.port.RefreshTokenStore;
import kr.co.csalgo.domain.auth.port.TokenCrypto;
import kr.co.csalgo.domain.auth.service.AuthCredentialService;
import kr.co.csalgo.domain.auth.type.CredentialType;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;
import kr.co.csalgo.domain.user.type.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminLoginUseCase {
	private final UserService userService;
	private final AuthCredentialService authCredentialService;
	private final TokenCrypto tokenCrypto;
	private final RefreshTokenStore refreshTokenStore;
	private final PasswordHasher passwordHasher;

	public TokenPair login(AdminLoginDto.Request request) {
		User user = userService.read(request.getEmail());
		if (user.getRole() != Role.ADMIN) {
			log.warn("권한이 없는 접근 시도: {}", user.getEmail());
			throw new CustomBusinessException(ErrorCode.FORBIDDEN_ACCESS);
		}

		AuthCredential authCredential = authCredentialService.read(user.getId(), CredentialType.PASSWORD);
		if (!passwordHasher.matches(request.getPassword(), authCredential.getPasswordHash())) {
			log.warn("잘못된 비밀번호 입력: {}", user.getEmail());
			throw new CustomBusinessException(ErrorCode.CREDENTIAL_NOT_FOUND);
		}

		String familyId = UUID.randomUUID().toString();
		String refresh = tokenCrypto.createInitialRefreshToken(user.getEmail(), familyId);
		Jws<Claims> parsed = tokenCrypto.parse(refresh);
		String jti = parsed.getPayload().getId();
		refreshTokenStore.initFamily(familyId, jti, parsed.getPayload().getExpiration().getTime());
		String access = tokenCrypto.createAccessToken(user.getEmail(), "ADMIN", familyId);
		log.info("관리자 로그인 성공: {}", user.getEmail());
		return new TokenPair(access, refresh);
	}
}
