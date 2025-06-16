package kr.co.csalgo.presentation.scheduler;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.mail.MessagingException;
import kr.co.csalgo.application.mail.usecase.RegisterQuestionResponseUseCase;
import kr.co.csalgo.application.mail.usecase.SendFeedbackMailUseCase;

class MailPollingSchedulerTest {
	private RegisterQuestionResponseUseCase registerQuestionResponseUseCase;
	private SendFeedbackMailUseCase sendFeedbackMailUseCase;
	private MailPollingScheduler mailPollingScheduler;

	@BeforeEach
	void setUp() {
		registerQuestionResponseUseCase = mock(RegisterQuestionResponseUseCase.class);
		sendFeedbackMailUseCase = mock(SendFeedbackMailUseCase.class);
		mailPollingScheduler = new MailPollingScheduler(registerQuestionResponseUseCase, sendFeedbackMailUseCase);
	}

	@Test
	@DisplayName("poll()이 호출되면 RegisterQuestionResponseUseCase.execute()가 실행되어야 한다")
	void testPollSuccess() throws MessagingException {
		// when
		mailPollingScheduler.poll();

		// then
		verify(registerQuestionResponseUseCase, times(1)).execute();
	}
}
