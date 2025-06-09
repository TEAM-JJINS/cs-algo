package kr.co.csalgo.presentation.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.csalgo.application.mail.CheckMailUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MailPollingScheduler {
	private final CheckMailUseCase checkMailUseCase;

	@Scheduled(cron = "0 */1 * * * *")
	public void poll() {
		checkMailUseCase.size();
	}
}
