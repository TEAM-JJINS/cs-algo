package kr.co.csalgo.web.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class EmailVerificationVerifyDto {
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Request {
		private String email;
		private String code;
		private VerificationCodeType type;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Response {
		private String message;
	}
}
