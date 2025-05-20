package kr.co.csalgo.application.auth.usecase;

import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.domain.auth.service.VerificationCodeService;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import kr.co.csalgo.infrastructure.email.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("EmailVerificationUseCase Test")
@ExtendWith(MockitoExtension.class)
public class EmailVerificationUseCaseTest {
    @Mock
    private VerificationCodeService verificationCodeService;
    @Mock
    private EmailService emailService;
    private EmailVerificationUseCase emailVerificationUseCase;

    @BeforeEach
    void setUp() {
        emailVerificationUseCase = new EmailVerificationUseCase(verificationCodeService, emailService);
    }

    @Test
    @DisplayName("이메일 인증번호 요청 테스트")
    void testCreateVerificationCodeSuccess() {
        String email = "test@example.com";
        String code = "123456";
        VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

        EmailVerificationCodeDto.Request request = EmailVerificationCodeDto.Request.builder()
                .email(email)
                .type(type)
                .build();

        when(verificationCodeService.create(email, type)).thenReturn(code);

        EmailVerificationCodeDto.Response response = emailVerificationUseCase.sendEmailVerificationCode(request);

        verify(verificationCodeService).create(email, type);
        verify(emailService).sendEmail(email, code);
        assertEquals("인증번호가 성공적으로 전송되었습니다.", response.getMessage());
    }

}
