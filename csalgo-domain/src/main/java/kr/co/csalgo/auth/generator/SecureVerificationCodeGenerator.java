package kr.co.csalgo.auth.generator;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class SecureVerificationCodeGenerator implements VerificationCodeGenerator {
	private static final SecureRandom secureRandom = new SecureRandom();

	@Override
	public String generate() {
		int code = secureRandom.nextInt(900000) + 100000;
		return String.valueOf(code);
	}
}
