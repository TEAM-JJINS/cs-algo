package kr.co.csalgo.application.problem.usecase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.common.util.MailTemplate;
import kr.co.csalgo.domain.email.EmailSender;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.service.QuestionSendingHistoryService;
import kr.co.csalgo.domain.question.service.QuestionService;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendQuestionMailUseCase {

	private final QuestionSendingHistoryService questionSendingHistoryService;
	private final QuestionService questionService;
	private final UserService userService;
	private final EmailSender emailSender;

	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	public SendQuestionMailDto.Response execute(SendQuestionMailDto.Request request) {
		Long questionId = request.getQuestionId();
		Long userId = request.getUserId();
		LocalDateTime scheduledTime = request.getScheduledTime();

		log.info("[문제 메일 발송 요청] questionId: {}, userId: {}, scheduledTime: {}", questionId, userId, scheduledTime);

		Question question = questionService.read(questionId);
		Runnable task = () -> sendToTarget(question, userId);

		if (scheduledTime != null) {
			long delay = Duration.between(LocalDateTime.now(), scheduledTime).toMillis();
			scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
			log.info("[예약 메일 전송 예약됨] delay(ms)={}", delay);
		} else {
			task.run();
		}

		return SendQuestionMailDto.Response.of();
	}

	private void sendToTarget(Question question, Long userId) {
		if (userId != null) {
			User user = userService.read(userId);
			sendMail(question, user);
		} else {
			log.info("[문제 메일 발송] 전체 사용자에게 발송 시작 - questionId: {}", question.getId());
			List<User> users = userService.list();
			users.forEach(user -> sendMail(question, user));
		}
	}

	private void sendMail(Question question, User user) {
		String subject = MailTemplate.QUESTION_MAIL_SUBJECT.formatted(question.getTitle());
		String body = question.getTitle();

		emailSender.send(user.getEmail(), subject, body);
		questionSendingHistoryService.create(question.getId(), user.getId());

		log.info("[문제 메일 발송 완료] questionId: {}, userId: {}", question.getId(), user.getId());
	}
}
