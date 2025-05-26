package kr.co.csalgo.infrastructure.email.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;

	public void sendEmail(String email, String code) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject("CS-ALGO 인증 코드");
			helper.setText("<h3>인증 코드</h3><p>" + code + "</p>", true);

			mailSender.send(message);
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
