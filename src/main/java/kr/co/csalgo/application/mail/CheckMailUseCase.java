package kr.co.csalgo.application.mail;

import kr.co.csalgo.domain.email.EmailReceiver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CheckMailUseCase {
	private final EmailReceiver emailReceiver;

	public int size() {
		return emailReceiver.receiveMessages().size();
	}
}
