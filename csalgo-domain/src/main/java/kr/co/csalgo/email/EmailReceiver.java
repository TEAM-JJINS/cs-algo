package kr.co.csalgo.email;

import java.util.List;

import jakarta.mail.Message;

public interface EmailReceiver {
	List<Message> receiveMessages();
}
