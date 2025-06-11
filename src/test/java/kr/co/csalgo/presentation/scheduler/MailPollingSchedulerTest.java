package kr.co.csalgo.presentation.scheduler;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.mail.MessagingException;
import kr.co.csalgo.application.mail.usecase.CheckMailUseCase;
import kr.co.csalgo.application.mail.usecase.RegisterQuestionResponseUseCase;

class MailPollingSchedulerTest {
	private CheckMailUseCase checkMailUseCase;
	private RegisterQuestionResponseUseCase registerQuestionResponseUseCase;
	private MailPollingScheduler mailPollingScheduler;

	@BeforeEach
	void setUp() {
		checkMailUseCase = mock(CheckMailUseCase.class);
		registerQuestionResponseUseCase = mock(RegisterQuestionResponseUseCase.class);
		mailPollingScheduler = new MailPollingScheduler(checkMailUseCase, registerQuestionResponseUseCase);
	}

	@Test
	@DisplayName("poll()이 호출되면 CheckMailUseCase.size()와 RegisterQuestionResponseUseCase.execute()가 실행되어야 한다")
	void testPollSuccess() throws MessagingException {
		// when
		mailPollingScheduler.poll();

		// then
		verify(checkMailUseCase, times(1)).size();
		verify(registerQuestionResponseUseCase, times(1)).execute();
	}
}
