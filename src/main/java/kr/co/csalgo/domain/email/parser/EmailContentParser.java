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
		if (message == null || getOriginalMessageId(message) == null) {
			return null;
		}

		String sender = extractSender(message);
		String title = extractTitle(message);
		String fullBody = extractTextFromMessage(message);
		String response = extractResponse(fullBody);
		String originalMessageId = getOriginalMessageId(message);

		return EmailParseResultDto.builder()
			.sender(sender)
			.title(title)
			.response(response)
			.originalMessageId(originalMessageId)
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
		if (fullBody == null || fullBody.isBlank()) {
			return "";
		}

		var pattern = java.util.regex.Pattern.compile("\\d{4}년\\s?\\d{1,2}월\\s?\\d{1,2}일\\s?작성[:：]?");
		var matcher = pattern.matcher(fullBody);
		if (matcher.find()) {
			return fullBody.substring(0, matcher.start()).trim();
		}

		String[] delimiters = {
			"-----Original Message-----",
			"-----Original message-----",
			"보낸 사람:",
			"From:",
			"On ",
			"님이 작성:",
			">"
		};

		for (String delimiter : delimiters) {
			int index = fullBody.indexOf(delimiter);
			if (index != -1) {
				return fullBody.substring(0, index).trim();
			}
		}

		return fullBody.trim();
	}

	private static String getOriginalMessageId(Message message) {
		try {
			String[] inReplyTo = message.getHeader("In-Reply-To");
			return inReplyTo != null && inReplyTo.length > 0 ? inReplyTo[0] : null;
		} catch (Exception e) {
			return null;
		}
	}
}
