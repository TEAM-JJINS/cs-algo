package kr.co.csalgo.application.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import kr.co.csalgo.domain.email.EmailReceiver;
import kr.co.csalgo.infrastructure.email.EmailContentParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParseMailUseCase {

	private final EmailReceiver emailReceiver;

	public List<String> parse() throws MessagingException {
		List<String> responses = new ArrayList<>();
		List<Message> messages = emailReceiver.receiveMessages();
		Collections.reverse(messages);

		for (Message message : messages) {
			log.info("본문 파싱 시작. subject={}", message.getSubject());
			String response = EmailContentParser.execute(message);
			log.info("본문 파싱 완료. subject={}, response={}", message.getSubject(), response);
			if (response.isBlank()) {
				continue;
			}
			responses.add(response);
			break;
		}
		return responses;
	}
}
