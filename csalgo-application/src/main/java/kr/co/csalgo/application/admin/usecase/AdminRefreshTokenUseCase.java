package kr.co.csalgo.application.admin.usecase;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.csalgo.application.admin.dto.AdminRefreshDto;
import kr.co.csalgo.application.admin.dto.TokenPair;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.auth.port.RefreshTokenStore;
import kr.co.csalgo.domain.auth.port.TokenCrypto;
import kr.co.csalgo.domain.user.type.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminRefreshTokenUseCase {
	private final TokenCrypto tokenCrypto;
	private final RefreshTokenStore refreshTokenStore;

	public TokenPair refresh(AdminRefreshDto.Request request) {
		Jws<Claims> parsed = tokenCrypto.parse(request.getRefreshToken());
		Claims claims = parsed.getPayload();
		if (!"refresh".equals(claims.get("typ", String.class))) {
			log.warn("잘못된 토큰 타입: {}", claims.get("typ", String.class));
			throw new CustomBusinessException(ErrorCode.INVALID_TOKEN_TYPE);
		}

		String familyId = claims.get("family", String.class);
		String oldJti = claims.getId();
		if (refreshTokenStore.isFamilyRevoked(familyId)) {
			log.warn("리프레시 토큰 패밀리가 취소됨: {}", familyId);
			throw new CustomBusinessException(ErrorCode.REFRESH_FAMILY_REVOKED);
		}

		String subject = claims.getSubject();
		String newRefreshToken = tokenCrypto.createRotatedRefreshToken(subject, familyId);
		Jws<Claims> newParsed = tokenCrypto.parse(newRefreshToken);
		String newJti = newParsed.getPayload().getId();

		boolean ok = refreshTokenStore.rotateIfLatest(familyId, oldJti, newJti, newParsed.getPayload().getExpiration().getTime());
		if (!ok) {
			refreshTokenStore.revokeFamily(familyId);
			log.warn("리프레시 토큰 재사용 감지: {}", familyId);
			throw new CustomBusinessException(ErrorCode.REFRESH_TOKEN_REUSE);
		}
		String newAccessToken = tokenCrypto.createAccessToken(subject, Role.ADMIN.name(), familyId);
		log.info("리프레시 토큰 재발급 성공: {}", subject);
		return new TokenPair(newAccessToken, newRefreshToken);
	}
}
