package kr.co.csalgo.infrastructure.email;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.mail.Message;

class JavaMailReceiverTest {
	@Test
	@DisplayName("receiveMessages - 실제 Gmail 계정으로부터 메시지를 성공적으로 가져오는 경우")
	void receiveMessages_shouldReturnMessagesFromServer() {
		String host = "imap.gmail.com";
		String username = System.getenv("MAIL_USERNAME");
		String password = System.getenv("MAIL_PASSWORD");

		System.out.println("Username: " + username);
		System.out.println("Password: " + password);

		JavaMailReceiver mailReceiver = new JavaMailReceiver(host, username, password);

		// when
		List<Message> messages = mailReceiver.receiveMessages();

		// then
		assertThat(messages).isNotNull();
	}
}

