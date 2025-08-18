package kr.co.csalgo.domain.auth.port;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface TokenCrypto {
	String createAccessToken(String subject, String role, String familyId);

	String createAccessToken(String subject, String role);

	String createInitialRefreshToken(String subject, String familyId);

	String createRotatedRefreshToken(String subject, String familyId);

	Jws<Claims> parse(String token);
}
