package kr.co.csalgo.domain.email;

public interface EmailSender {
	void send(String to, String subject, String content);
}
