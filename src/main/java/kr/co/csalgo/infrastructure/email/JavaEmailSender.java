package kr.co.csalgo.infrastructure.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.email.EmailSender;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JavaEmailSender implements EmailSender {
	private final JavaMailSender mailSender;

	@Override
	public void send(String to, String subject, String content) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			mailSender.send(message);
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.EMAIL_SENDER_ERROR);
		}
	}
}
