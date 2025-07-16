package kr.co.csalgo.infrastructure.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.email.EmailSender;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JavaEmailSender implements EmailSender {
	private final JavaMailSender mailSender;

	@Override
	@Async
	public void send(String to, String subject, String content) {
		doSend(to, subject, content, null);
	}

	@Override
	public void sendReply(String to, String subject, String content, String originalMessageId) {
		doSend(to, subject, content, originalMessageId);
	}

	private void doSend(String to, String subject, String content, String originalMessageId) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			if (originalMessageId != null) {
				message.setHeader("In-Reply-To", originalMessageId);
				message.setHeader("References", originalMessageId);
			}
			mailSender.send(message);
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.EMAIL_SENDER_ERROR);
		}
	}
}
