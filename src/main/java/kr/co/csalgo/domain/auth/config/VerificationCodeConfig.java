package kr.co.csalgo.domain.auth.config;

import kr.co.csalgo.domain.auth.repository.ConcurrentMapVerificationCodeRepository;
import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VerificationCodeConfig {

    @Value("${verification.code.expiration}")
    private long codeExpiration;

    @Bean
    public VerificationCodeRepository verificationCodeRepository() {
        return new ConcurrentMapVerificationCodeRepository(codeExpiration);
    }
}
