package kr.co.csalgo.infrastructure.email;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;

class JavaEmailSenderTest {

	private JavaMailSender javaMailSender;
	private JavaEmailSender javaEmailSender;

	@BeforeEach
	void setUp() {
		javaMailSender = mock(JavaMailSender.class);
		javaEmailSender = new JavaEmailSender(javaMailSender);
	}

	@Test
	@DisplayName("send - 정상적으로 메일이 전송되어야 한다")
	void send_shouldSendEmailSuccessfully() throws Exception {
		// given
		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

		// when & then
		assertThatCode(() -> javaEmailSender.send("test@example.com", "제목", "내용"))
			.doesNotThrowAnyException();

		verify(javaMailSender, times(1)).send(mimeMessage);
	}

	@Test
	@DisplayName("send - 예외 발생 시 CustomBusinessException이 발생해야 한다")
	void send_shouldThrowExceptionWhenErrorOccurs() {
		// given
		when(javaMailSender.createMimeMessage()).thenThrow(new RuntimeException("메일 생성 실패"));

		// when & then
		assertThatThrownBy(() -> javaEmailSender.send("test@example.com", "제목", "내용"))
			.isInstanceOf(CustomBusinessException.class)
			.hasMessage(ErrorCode.EMAIL_SENDER_ERROR.getMessage());
	}
}
