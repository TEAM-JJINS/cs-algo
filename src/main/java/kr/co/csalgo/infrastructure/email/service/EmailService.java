package kr.co.csalgo.infrastructure.email.service;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.config.EmailConfig;
import kr.co.csalgo.domain.email.EmailReceiver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailReceiver {
	private final EmailConfig emailConfig;
	private final JavaMailSender mailSender;

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
			Properties props = new Properties();
			props.put("mail.store.protocol", "imaps");

			Session session = Session.getInstance(props);
			Store store = session.getStore("imaps");
			store.connect(emailConfig.getMailHost(), emailConfig.getMailUsername(), emailConfig.getMailPassword());

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			return Arrays.asList(inbox.getMessages());
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.EMAIL_RECEIVER_ERROR);
		}
	}
}
