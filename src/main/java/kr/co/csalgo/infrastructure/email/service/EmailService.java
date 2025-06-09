package kr.co.csalgo.infrastructure.email.service;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.email.EmailReceiver;
import kr.co.csalgo.infrastructure.email.JavaMailReceiver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailReceiver {
	private final JavaMailSender mailSender;
	private final JavaMailReceiver mailReceiver;

	public void sendEmail(String email, String subject, String content) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(content, true);

			mailSender.send(message);
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<Message> receiveMessages() {
		try {
			return mailReceiver.receiveMessages();
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.EMAIL_RECEIVER_ERROR);
		}
	}
}
