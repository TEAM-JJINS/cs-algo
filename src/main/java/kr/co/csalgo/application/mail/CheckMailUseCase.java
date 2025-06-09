package kr.co.csalgo.application.mail;

import org.springframework.stereotype.Service;

import kr.co.csalgo.domain.email.EmailReceiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckMailUseCase {
	private final EmailReceiver emailReceiver;

	public int size() {
		int size = emailReceiver.receiveMessages().size();
		log.info("{}개의 이메일이 수신되었습니다.", size);
		return size;
	}
}
