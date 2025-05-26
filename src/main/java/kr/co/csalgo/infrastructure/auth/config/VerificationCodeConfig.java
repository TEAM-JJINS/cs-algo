package kr.co.csalgo.infrastructure.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.infrastructure.auth.repository.ConcurrentMapVerificationCodeRepository;

@Configuration
public class VerificationCodeConfig {

	@Value("${verification.code.expiration}")
	private long codeExpiration;

	@Bean
	public VerificationCodeRepository verificationCodeRepository() {
		return new ConcurrentMapVerificationCodeRepository(codeExpiration);
	}
}
