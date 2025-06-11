package kr.co.csalgo.application.mail.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.mail.Message;
import kr.co.csalgo.domain.email.EmailReceiver;

@ExtendWith(MockitoExtension.class)
class CheckMailUseCaseTest {
	@Mock
	private EmailReceiver mailReceiver;

	private CheckMailUseCase checkMailUseCase;

	@BeforeEach
	void setUp() {
		checkMailUseCase = new CheckMailUseCase(mailReceiver);
	}

	@Test
	@DisplayName("이메일 수신 확인 기능이 메시지 개수를 반환해야 합니다.")
	void sizeShouldReturnMessageCount() {
		// given
		Message message = mock(Message.class);
		when(mailReceiver.receiveMessages()).thenReturn(List.of(message));

		// when
		int size = checkMailUseCase.size();

		// then
		assertThat(size).isEqualTo(1);
	}
}
