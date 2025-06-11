package kr.co.csalgo.application.mail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailParseResultDto {
	private final String sender;
	private final String title;
	private final String response;

	@Builder
	public EmailParseResultDto(String sender, String title, String response) {
		this.sender = sender;
		this.title = title;
		this.response = response;
	}
}
