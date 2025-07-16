package kr.co.csalgo.application.mail.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.mail.Flags;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import kr.co.csalgo.email.EmailContent;
import kr.co.csalgo.email.EmailContentParser;
import kr.co.csalgo.email.EmailReceiver;
import kr.co.csalgo.question.entity.Question;
import kr.co.csalgo.question.entity.QuestionResponse;
import kr.co.csalgo.question.service.QuestionResponseService;
import kr.co.csalgo.question.service.QuestionService;
import kr.co.csalgo.user.entity.User;
import kr.co.csalgo.user.service.UserService;
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
		int successCount = 0;
		int failCount = 0;
		for (Message message : messages) {
			EmailContent result = EmailContentParser.parse(message);
			if (result == null) {
				log.warn("본문 파싱 실패: 읽음 처리 후 건너뜀");
				message.setFlag(Flags.Flag.SEEN, true);
				failCount++;
				continue;
			}
			log.info("본문 파싱 완료: subject={}, sender={}, response={}", result.getTitle(), result.getSender(), result.getResponse());
			User user = userService.read(result.getSender());
			Question question = questionService.read(result.getTitle());
			QuestionResponse questionResponse = questionResponseService.create(question, user, result.getResponse(), result.getMessageId());
			log.info("QuestionResponse 저장 완료. questionResponseId={}", questionResponse.getId());

			message.setFlag(Flags.Flag.SEEN, true);
			successCount++;
		}
		log.info("이메일 처리 완료. 총 수신: {}, 처리 성공: {}, 실패: {}", messages.size(), successCount, failCount);
	}

}
