package kr.co.csalgo.presentation.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import kr.co.csalgo.application.mail.usecase.RegisterQuestionResponseUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MailPollingScheduler {
	private final RegisterQuestionResponseUseCase registerQuestionResponseUseCase;

	@Scheduled(cron = "0 */1 * * * *")
	public void poll() throws MessagingException {
		registerQuestionResponseUseCase.execute();
	}
}
