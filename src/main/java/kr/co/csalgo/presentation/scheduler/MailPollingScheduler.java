package kr.co.csalgo.presentation.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import kr.co.csalgo.application.mail.usecase.RegisterQuestionResponseUseCase;
import kr.co.csalgo.application.mail.usecase.SendFeedbackMailUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MailPollingScheduler {
	private final RegisterQuestionResponseUseCase registerQuestionResponseUseCase;
	private final SendFeedbackMailUseCase sendFeedbackMailUseCase;

	@Scheduled(cron = "0 */1 * * * *")
	public void poll() throws MessagingException {
		registerQuestionResponseUseCase.execute();
		sendFeedbackMailUseCase.execute();
	}
}
