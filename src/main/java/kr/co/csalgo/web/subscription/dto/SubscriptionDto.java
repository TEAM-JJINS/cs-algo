package kr.co.csalgo.web.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class SubscriptionDto {
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Request {
		private String email;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Response {
		private String message;
	}
}
