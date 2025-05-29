package kr.co.csalgo.web.subscribe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailVerificationDto {
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Request {
		private String email;
		private VerificationCodeType type;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Response {
		private String message;
	}
}
