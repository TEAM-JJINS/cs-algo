package kr.co.csalgo.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import kr.co.csalgo.domain.auth.port.PasswordHasher;

@Component
public class BCryptPasswordHasher implements PasswordHasher {
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public String encode(String raw) {
		return encoder.encode(raw);
	}

	@Override
	public boolean matches(String raw, String encoded) {
		return encoder.matches(raw, encoded);
	}
}
