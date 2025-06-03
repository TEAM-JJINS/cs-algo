package kr.co.csalgo.application.problem.usecase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.service.QuestionSendingHistoryService;
import kr.co.csalgo.domain.question.service.QuestionService;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;
import kr.co.csalgo.infrastructure.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendQuestionMailUseCase {
	private final QuestionSendingHistoryService questionSendingHistoryService;
	private final QuestionService questionService;
	private final UserService userService;
	private final EmailService mailService;
	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	public SendQuestionMailDto.Response execute(SendQuestionMailDto.Request request) {
		Long questionId = request.getQuestionId();
		Long userId = request.getUserId();
		LocalDateTime scheduledTime = request.getScheduledTime();

		log.info("[문제 메일 발송 요청] qusetionId: {}, userId: {}, scheduledTime: {}", questionId, userId, scheduledTime);

		Question question = questionService.read(questionId);
		User user = userId != null ? userService.read(userId) : null;

		Runnable task = () -> {
			if (user != null) {
				// TODO: 메일 발송 로직 추가 필요
				questionSendingHistoryService.create(question.getId(), user.getId());
				log.info("[문제 메일 발송 완료] questionId: {}, userId: {}", question.getId(), user.getId());
			} else {
				log.info("[문제 메일 발송] 전체 사용자에게 발송 예정 - questionId: {}", question.getId());
				List<User> users = userService.list();
				for (User u : users) {
					// TODO: 메일 발송 로직 추가 필요
					questionSendingHistoryService.create(question.getId(), u.getId());
					log.info("[문제 메일 발송 완료] questionId: {}, userId: {}", question.getId(), u.getId());
				}
			}
		};

		if (request.getScheduledTime() != null) {
			long delay = Duration.between(LocalDateTime.now(), request.getScheduledTime()).toMillis();
			scheduledExecutorService.schedule(task, delay, TimeUnit.MILLISECONDS);
			log.info("[예약 메일 전송 예약됨] delay(ms)={}", delay);
		} else {
			task.run();
		}

		return SendQuestionMailDto.Response.of();
	}
}
