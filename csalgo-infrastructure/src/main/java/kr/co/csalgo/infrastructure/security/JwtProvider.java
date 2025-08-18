package kr.co.csalgo.infrastructure.security;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.co.csalgo.domain.auth.port.TokenCrypto;

@Component
public class JwtProvider implements TokenCrypto {
	private final SecretKey key;
	private final long accessTtlMs;
	private final long refreshTtlMs;

	public JwtProvider(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.access-ttl-ms:600000}") long accessTtlMs,
		@Value("${jwt.refresh-ttl-ms:2592000000}") long refreshTtlMs
	) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
		this.accessTtlMs = accessTtlMs;
		this.refreshTtlMs = refreshTtlMs;
	}

	@Override
	public String createAccessToken(String subject, String role, String familyId) {
		Instant now = Instant.now();
		return Jwts.builder()
			.setSubject(subject)
			.claim("role", role)
			.claim("typ", "access")
			.claim("family", familyId)
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(now.plusMillis(accessTtlMs)))
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	@Override
	public String createAccessToken(String subject, String role) {
		return createAccessToken(subject, role, null);
	}

	@Override
	public String createInitialRefreshToken(String subject, String familyId) {
		return createRefreshToken(subject, familyId, UUID.randomUUID().toString());
	}

	@Override
	public String createRotatedRefreshToken(String subject, String familyId) {
		return createRefreshToken(subject, familyId, UUID.randomUUID().toString());
	}

	private String createRefreshToken(String subject, String familyId, String jti) {
		Instant now = Instant.now();
		return Jwts.builder()
			.setSubject(subject)
			.claim("typ", "refresh")
			.claim("family", familyId)
			.setId(jti)
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(now.plusMillis(refreshTtlMs)))
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	@Override
	public Jws<Claims> parse(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build().parseSignedClaims(token);
	}
}
