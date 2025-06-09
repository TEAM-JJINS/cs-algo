package kr.co.csalgo.infrastructure.email;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.mail.Message;
import kr.co.csalgo.infrastructure.email.config.MailProperties;

class JavaMailReceiverTest {
	@Test
	@DisplayName("receiveMessages - 실제 Gmail 계정으로부터 메시지를 성공적으로 가져오는 경우")
	void receiveMessages_shouldReturnMessagesFromServer() {
		String host = "imap.gmail.com";
		String username = System.getenv("MAIL_USERNAME");
		String password = System.getenv("MAIL_PASSWORD");

		MailProperties properties = new MailProperties();
		properties.setHost(host);
		properties.setUsername(username);
		properties.setPassword(password);

		JavaEmailReceiver mailReceiver = new JavaEmailReceiver(properties);

		// when
		List<Message> messages = mailReceiver.receiveMessages();

		// then
		assertThat(messages).isNotNull();
	}
}

