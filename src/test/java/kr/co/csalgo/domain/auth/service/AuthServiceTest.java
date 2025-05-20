package kr.co.csalgo.domain.auth.service;

import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("AuthService Test")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private VerificationCodeRepository verificationCodeRepository;
    @Mock
    private JavaMailSender javaMailSender;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(verificationCodeRepository, javaMailSender);
    }

    @Test
    @DisplayName("정상적으로 인증 코드 생성 후 저장되고 이메일이 전송된다.")
    void testCreateSuccess() {
        String email = "test@example.com";
        VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        authService.create(email, type);

        verify(verificationCodeRepository).create(eq(email), anyString(), eq(type));
        verify(javaMailSender).send(message);
    }

    @Test
    @DisplayName("저장 실패 시 예외가 발생한다.")
    void testSaveFail() {
        doThrow(new IllegalStateException("인증 코드 저장 실패")).when(verificationCodeRepository)
                .create(any(), any(), any());

        assertThrows(IllegalStateException.class, () ->
                authService.create("fail@example.com", VerificationCodeType.SUBSCRIPTION));
    }

    @Test
    @DisplayName("이메일 전송 실패 시 예외가 발생한다.")
    void testSendEmailFail() {
        String email = "fail@example.com";
        VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);
        doThrow(new IllegalStateException("이메일 전송 실패")).when(javaMailSender).send(any(MimeMessage.class));

        assertThrows(IllegalStateException.class, () ->
                authService.create(email, type));
    }

    @Test
    @DisplayName("생성된 인증 코드는 6자리 숫자 문자열이다.")
    void testGenerateCode() {
        String email = "test@example.com";
        VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        authService.create(email, type);

        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        verify(verificationCodeRepository).create(eq(email), codeCaptor.capture(), eq(type));
        String generatedCode = codeCaptor.getValue();

        assertTrue(generatedCode.matches("\\d{6}"));
    }
}