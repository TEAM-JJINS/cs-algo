package kr.co.csalgo.application.mail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailParseResultDto {
	private final String sender;
	private final String content;

	@Builder
	public EmailParseResultDto(String sender, String content) {
		this.sender = sender;
		this.content = content;
	}
}
