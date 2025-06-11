package kr.co.csalgo.infrastructure.email;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.InternetAddress;
import kr.co.csalgo.application.mail.dto.EmailParseResultDto;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;

public class EmailContentParser {

	public static EmailParseResultDto execute(Message message) {
		if (message == null || !isReply(message)) {
			return null;
		}

		String sender = extractSender(message);
		String fullBody = extractTextFromMessage(message);
		String content = extractResponse(fullBody);

		return EmailParseResultDto.builder()
			.sender(sender)
			.content(content)
			.build();
	}

	private static String extractTextFromMessage(Message message) {
		try {
			Object content = message.getContent();
			if (content instanceof String) {
				return (String)content;
			} else if (content instanceof Multipart multipart) {
				for (int i = 0; i < multipart.getCount(); i++) {
					BodyPart part = multipart.getBodyPart(i);
					if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
						continue;
					}
					if (part.isMimeType("text/plain")) {
						return (String)part.getContent();
					}
				}
			}
			throw new CustomBusinessException(ErrorCode.EMAIL_CONTENT_NOT_FOUND);
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.EMAIL_CONTENT_PARSE_FAIL);
		}
	}

	private static String extractSender(Message message) {
		try {
			return ((InternetAddress)message.getFrom()[0]).getAddress();
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.EMAIL_SENDER_PARSE_FAIL);
		}
	}

	private static String extractResponse(String fullBody) {
		String[] lines = fullBody.split("\r?\n");

		StringBuilder response = new StringBuilder();
		for (String line : lines) {
			if (line.trim().matches("(?i)^\\d{4}년.*작성:.*")
				|| line.trim().startsWith(">")
				|| line.trim().startsWith("On ")
				|| line.trim().startsWith("From:")
				|| line.trim().contains("님이 작성:")) {
				break;
			}
			response.append(line).append("\n");
		}
		return response.toString().trim();
	}

	private static boolean isReply(Message message) {
		try {
			String[] inReplyTo = message.getHeader("In-Reply-To");
			return inReplyTo != null && inReplyTo.length > 0;
		} catch (Exception e) {
			return false;
		}
	}
}
