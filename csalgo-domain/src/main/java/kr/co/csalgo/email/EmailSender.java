package kr.co.csalgo.email;

public interface EmailSender {
	void send(String to, String subject, String content);

	void sendReply(String to, String subject, String content, String inReplyTo);
}
