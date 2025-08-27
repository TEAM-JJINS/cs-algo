package kr.co.csalgo.application.admin.usecase;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.csalgo.application.admin.dto.AdminRefreshDto;
import kr.co.csalgo.common.message.CommonResponse;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.auth.port.RefreshTokenStore;
import kr.co.csalgo.domain.auth.port.TokenCrypto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminLogoutUseCase {
	private final TokenCrypto tokenCrypto;
	private final RefreshTokenStore refreshTokenStore;

	public CommonResponse logout(AdminRefreshDto.Request request) {
		String refreshToken = request.getRefreshToken();
		Jws<Claims> parsed = tokenCrypto.parse(refreshToken);
		String familyId = parsed.getPayload().get("family", String.class);

		refreshTokenStore.revokeFamily(familyId);
		log.info("관리자 로그아웃 성공: familyId={}", familyId);

		return new CommonResponse(MessageCode.LOGOUT_SUCCESS.getMessage());
	}
}
