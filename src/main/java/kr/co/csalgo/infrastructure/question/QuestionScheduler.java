package kr.co.csalgo.infrastructure.question;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.csalgo.application.problem.usecase.SendDailyQuestionMailUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionScheduler {

	private final SendDailyQuestionMailUseCase sendDailyQuestionMailUseCase;

	@Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
	public void run() {
		sendDailyQuestionMailUseCase.execute();
	}
}

