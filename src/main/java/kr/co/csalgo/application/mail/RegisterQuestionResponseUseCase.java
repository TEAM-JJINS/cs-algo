package kr.co.csalgo.application.mail;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import kr.co.csalgo.application.mail.dto.EmailParseResultDto;
import kr.co.csalgo.domain.email.EmailReceiver;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.service.QuestionResponseService;
import kr.co.csalgo.domain.question.service.QuestionService;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;
import kr.co.csalgo.infrastructure.email.EmailContentParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterQuestionResponseUseCase {

	private final EmailReceiver emailReceiver;
	private final QuestionResponseService questionResponseService;
	private final UserService userService;
	private final QuestionService questionService;

	public void execute() throws MessagingException {
		List<Message> messages = emailReceiver.receiveMessages();
		Collections.reverse(messages);

		for (Message message : messages) {
			EmailParseResultDto result = EmailContentParser.execute(message);
			if (result == null) {
				continue;
			}
			log.info("본문 파싱 완료: subject={}, sender={}, response={}", result.getTitle(), result.getSender(), result.getResponse());
			User user = userService.read(result.getSender());
			Question question = questionService.read(result.getTitle()); //임시방편
			QuestionResponse questionResponse = questionResponseService.create(question, user, result.getResponse());
			log.info("QuestionResponse 저장 완료. questionResponseId={}", questionResponse.getId());

		}
	}
}
