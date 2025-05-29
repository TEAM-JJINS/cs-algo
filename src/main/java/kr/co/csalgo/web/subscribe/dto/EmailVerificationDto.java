package kr.co.csalgo.web.subscribe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class EmailVerificationDto {
	@AllArgsConstructor
	public static class Request {
		private String email;
		private VerificationCodeType type;
	}

	@AllArgsConstructor
	@Getter
	public static class Response {
		private String message;
	}
}
