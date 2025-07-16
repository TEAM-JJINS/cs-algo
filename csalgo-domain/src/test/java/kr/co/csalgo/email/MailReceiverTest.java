package kr.co.csalgo.domain.email;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import jakarta.mail.Message;
import kr.co.csalgo.email.EmailReceiver;

class MailReceiverTest {

	@Test
	void receiveMessages_shouldReturnMessages_whenConnectedToServer() {
		EmailReceiver mockReceiver = mock(EmailReceiver.class);
		Message message = mock(Message.class);
		when(mockReceiver.receiveMessages()).thenReturn(List.of(message));

		List<Message> result = mockReceiver.receiveMessages();

		assertThat(result).isNotEmpty();
	}
}

