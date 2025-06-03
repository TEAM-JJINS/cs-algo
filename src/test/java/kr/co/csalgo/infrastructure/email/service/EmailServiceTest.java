package kr.co.csalgo.infrastructure.email.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;

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

		CustomBusinessException exception = assertThrows(CustomBusinessException.class, () ->
			emailService.sendVerificationCode(email, code));

		assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
	}
}
