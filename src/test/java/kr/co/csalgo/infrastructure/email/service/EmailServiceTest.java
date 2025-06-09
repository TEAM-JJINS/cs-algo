package kr.co.csalgo.infrastructure.email.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.config.EmailConfig;

@DisplayName("EmailService Test")
@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

	@Mock
	private JavaMailSender mailSender;

	private EmailService emailService;

	@Mock
	private EmailConfig emailConfig;

	@BeforeEach
	void setUp() {
		emailService = new EmailService(emailConfig, mailSender);
	}

	@Test
	@DisplayName("sendEmail - 성공적으로 전송되는 경우")
	void testSendEmailSuccess() {
		// given
		String email = "user@example.com";
		String subject = "Test Subject";
		String content = "<h1>Hello</h1>";

		MimeMessage message = mock(MimeMessage.class);
		when(mailSender.createMimeMessage()).thenReturn(message);

		// when & then (예외 없이 정상 실행)
		assertDoesNotThrow(() -> emailService.sendEmail(email, subject, content));
		verify(mailSender).send(message);
	}

	@Test
	@DisplayName("sendEmail - 예외 발생 시 CustomBusinessException 발생")
	void testSendEmailFail() {
		// given
		String email = "fail@example.com";
		String subject = "Fail";
		String content = "Something bad happened";

		when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("MimeMessage 생성 실패"));

		// when & then
		CustomBusinessException exception = assertThrows(CustomBusinessException.class,
			() -> emailService.sendEmail(email, subject, content));

		assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
	}
}
