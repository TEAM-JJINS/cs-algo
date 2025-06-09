package kr.co.csalgo.presentation.scheduler;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.csalgo.application.mail.CheckMailUseCase;

class MailPollingSchedulerTest {

	private CheckMailUseCase checkMailUseCase;
	private MailPollingScheduler mailPollingScheduler;

	@BeforeEach
	void setUp() {
		checkMailUseCase = mock(CheckMailUseCase.class);
		mailPollingScheduler = new MailPollingScheduler(checkMailUseCase);
	}

	@Test
	@DisplayName("poll()이 호출되면 CheckMailUseCase.size()가 실행되어야 한다")
	void poll_shouldInvokeCheckMailUseCaseSize() {
		// when
		mailPollingScheduler.poll();

		// then
		verify(checkMailUseCase, times(1)).size();
	}
}
