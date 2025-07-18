package kr.co.csalgo.infrastructure.email;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.FlagTerm;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.email.EmailReceiver;
import kr.co.csalgo.infrastructure.email.config.MailProperties;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JavaEmailReceiver implements EmailReceiver {
	private final MailProperties properties;

	@Override
	public List<Message> receiveMessages() {
		try {
			Properties props = new Properties();
			props.put("mail.store.protocol", "imaps");

			Session session = Session.getInstance(props);
			Store store = session.getStore("imaps");
			store.connect(properties.getHost(), properties.getUsername(), properties.getPassword());

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			FlagTerm unseenFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			Message[] unreadMessages = inbox.search(unseenFlagTerm);

			return Arrays.asList(unreadMessages);
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.EMAIL_RECEIVER_ERROR);
		}
	}
}
