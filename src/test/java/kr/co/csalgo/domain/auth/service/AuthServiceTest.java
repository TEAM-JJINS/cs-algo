package kr.co.csalgo.domain.auth.service;

import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@DisplayName("AuthService Test")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private VerificationCodeRepository verificationCodeRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(verificationCodeRepository);
    }

    @Test
    @DisplayName("인증 코드를 성공적으로 저장한다.")
    void saveCodeSuccess() {
        String email = "test@example.com";
        String code = "123456";
        VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

        authService.save(email, code, type);

        verify(verificationCodeRepository).create(email, code, type);
    }
}