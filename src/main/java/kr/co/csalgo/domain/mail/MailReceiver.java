package kr.co.csalgo.domain.mail;

import java.util.List;

import jakarta.mail.Message;

public interface MailReceiver {
	List<Message> receiveMessages();
}
