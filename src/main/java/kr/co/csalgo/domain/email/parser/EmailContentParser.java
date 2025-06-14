package kr.co.csalgo.domain.email.parser;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.InternetAddress;
import kr.co.csalgo.application.mail.dto.EmailParseResultDto;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;

public class EmailContentParser {

	public static EmailParseResultDto parse(Message message) {
		if (message == null || !isReply(message)) {
			return null;
		}

		String sender = extractSender(message);
		String title = extractTitle(message);
		String fullBody = extractTextFromMessage(message);
		String response = extractResponse(fullBody);

		return EmailParseResultDto.builder()
			.sender(sender)
			.title(title)
			.response(response)
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

	private static String extractTitle(Message message) {
		try {
			String subject = message.getSubject();
			if (subject == null) {
				return "";
			}
			subject = subject.replaceFirst("(?i)^Re:\\s*", "");
			subject = subject.replaceFirst("^\\[CS-ALGO\\]\\s*", "");
			return subject.trim();
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.EMAIL_SUBJECT_PARSE_FAIL);
		}
	}

	private static String extractResponse(String fullBody) {
		String[] lines = fullBody.split("\r?\n");
		StringBuilder response = new StringBuilder();

		for (String line : lines) {
			String trimmed = line.trim();
			if ((trimmed.contains("년") && trimmed.contains("작성:"))
				|| trimmed.startsWith(">")
				|| trimmed.startsWith("On ")
				|| trimmed.startsWith("From:")
				|| trimmed.contains("님이 작성:")) {
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
