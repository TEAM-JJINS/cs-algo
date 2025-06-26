package kr.co.csalgo.application.mail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailParseResultDto {
	private final String sender;
	private final String title;
	private final String response;
	private final String messageId;

	@Builder
	public EmailParseResultDto(String sender, String title, String response, String messageId) {
		this.sender = sender;
		this.title = title;
		this.response = response;
		this.messageId = messageId;
	}
}
