package kr.co.csalgo.domain.email;

import java.util.List;

import jakarta.mail.Message;

public interface EmailReceiver {
	List<Message> receiveMessages();
}
