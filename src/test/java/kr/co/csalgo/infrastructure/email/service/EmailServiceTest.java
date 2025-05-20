package kr.co.csalgo.infrastructure.email.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("EmailService Test")
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(javaMailSender);
    }

    @Test
    @DisplayName("이메일 전송 실패 시 예외가 발생한다.")
    void testSendEmailFail() {
        String email = "fail@example.com";
        String code = "123456";

        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);
        doThrow(new MailSendException("전송 오류")).when(javaMailSender).send(any(MimeMessage.class));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                emailService.sendEmail(email, code));

        assertEquals("이메일 전송에 실패했습니다.", exception.getMessage());
    }
}
