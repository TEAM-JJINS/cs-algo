package kr.co.csalgo.infrastructure.email;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import jakarta.mail.Message;

@ActiveProfiles("test")
class JavaMailReceiverTest {
	@Test
	@DisplayName("receiveMessages - mock으로 메서드 행위만 테스트")
	void receiveMessages_shouldReturnMockMessages() throws Exception {
		// given
		JavaEmailReceiver mailReceiver = mock(JavaEmailReceiver.class);
		Message mockMessage = mock(Message.class);

		when(mockMessage.getSubject()).thenReturn("Test Subject");
		when(mockMessage.getContent()).thenReturn("Test Content");

		when(mailReceiver.receiveMessages()).thenReturn(List.of(mockMessage));

		// when
		List<Message> messages = mailReceiver.receiveMessages();

		// then
		assertThat(messages).isNotNull();
		assertThat(messages).hasSize(1);
		assertThat(messages.get(0).getSubject()).isEqualTo("Test Subject");
	}
}
