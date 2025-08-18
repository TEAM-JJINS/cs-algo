package kr.co.csalgo.application.admin.port;

import kr.co.csalgo.application.admin.dto.TokenPair;

public interface TokenIssuer {
	String issueAccessToken(String subject, String role);

	TokenPair issueInitialTokens(String subject, String role);

	TokenPair rotate(String refreshToken);
}
