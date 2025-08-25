package kr.co.csalgo.web.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageResponseDto {
	private String message;

	@Builder
	public MessageResponseDto(String message) {
		this.message = message;
	}
}
