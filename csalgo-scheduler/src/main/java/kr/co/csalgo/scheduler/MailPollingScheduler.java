package kr.co.csalgo.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

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
	@SchedulerLock(
		name = "mailPollingScheduler",
		lockAtMostFor = "3m",
		lockAtLeastFor = "1m"
	)
	public void poll() throws MessagingException {
		registerQuestionResponseUseCase.execute();
		sendFeedbackMailUseCase.execute();
	}
}
