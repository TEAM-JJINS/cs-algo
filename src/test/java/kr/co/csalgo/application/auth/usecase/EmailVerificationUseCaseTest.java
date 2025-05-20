package kr.co.csalgo.application.auth.usecase;

import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.domain.auth.service.AuthService;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@DisplayName("EmailVerificationUseCase Test")
@ExtendWith(MockitoExtension.class)
public class EmailVerificationUseCaseTest {
    @Mock
    private AuthService authService;
    private EmailVerificationUseCase emailVerificationUseCase;

    @BeforeEach
    void setUp() {
        emailVerificationUseCase = new EmailVerificationUseCase(authService);
    }

    @Test
    @DisplayName("이메일 인증번호 요청 테스트")
    void testCreateVerificationCodeSuccess() {
        String email = "test@example.com";
        EmailVerificationCodeDto.Request request = EmailVerificationCodeDto.Request.builder()
                .email(email)
                .type(VerificationCodeType.SUBSCRIPTION)
                .build();

        EmailVerificationCodeDto.Response response = emailVerificationUseCase.sendEmailVerificationCode(request);

        verify(authService).create(email, VerificationCodeType.SUBSCRIPTION);
    }

}
