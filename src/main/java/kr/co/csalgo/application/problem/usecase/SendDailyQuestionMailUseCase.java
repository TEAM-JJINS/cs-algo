package kr.co.csalgo.application.problem.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.csalgo.common.util.MailTemplate;
import kr.co.csalgo.email.EmailSender;
import kr.co.csalgo.question.entity.Question;
import kr.co.csalgo.question.service.QuestionSendingHistoryService;
import kr.co.csalgo.question.service.QuestionService;
import kr.co.csalgo.user.entity.User;
import kr.co.csalgo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendDailyQuestionMailUseCase {
	private final UserService userService;
	private final QuestionService questionService;
	private final QuestionSendingHistoryService questionSendingHistoryService;
	private final EmailSender emailSender;

	public void execute() {
		log.info("[스케쥴러 문제 전송 시작] 전체 유저 대상");
		List<Question> allQuestions = questionService.list();

		if (allQuestions.isEmpty()) {
			log.warn("[문제 전송 스킵] 등록된 문제가 없음.");
			return;
		}

		List<User> users = userService.list();
		for (User user : users) {
			try {
				Question selected = selectQuestion(user, allQuestions);
				sendMail(selected, user);

			} catch (Exception e) {
				log.error("[문제 전송 실패] userId: {}", user.getId(), e);
			}
		}

	}

	private Question selectQuestion(User user, List<Question> questions) {
		long sentCount = questionSendingHistoryService.count(user);
		int index = (int)(sentCount % questions.size());
		Question selected = questions.get(index);
		log.info("[문제 선택] questionId: {}, userId: {}", selected.getId(), user.getId());
		return selected;
	}

	private void sendMail(Question question, User user) {
		String subject = MailTemplate.QUESTION_MAIL_SUBJECT.formatted(question.getTitle());
		String body = MailTemplate.formatQuestionMailBody(
			question.getTitle(),
			question.getId(),
			user.getUuid()
		);

		emailSender.send(user.getEmail(), subject, body);
		questionSendingHistoryService.create(question.getId(), user.getId());

		log.info("[문제 메일 발송 완료] questionId: {}, userId: {}", question.getId(), user.getId());
	}
}
