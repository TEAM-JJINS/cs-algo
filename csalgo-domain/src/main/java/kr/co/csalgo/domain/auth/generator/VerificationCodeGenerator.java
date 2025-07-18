package kr.co.csalgo.domain.auth.generator;

import org.springframework.stereotype.Component;

@Component
public interface VerificationCodeGenerator {
	String generate();
}
