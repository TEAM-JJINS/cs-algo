package kr.co.csalgo.web.security;

import java.util.Collections;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtValidator {
	private final SecretKey key;

	public JwtValidator(@Value("${jwt.secret}") String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
		String email = claims.getSubject();
		String role = claims.get("role", String.class);
		return new UsernamePasswordAuthenticationToken(email, "", Collections.singleton(new SimpleGrantedAuthority(role)));
	}
}

