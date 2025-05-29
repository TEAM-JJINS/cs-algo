package kr.co.csalgo.web.subscribe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SubscriptionDto {
	@AllArgsConstructor
	public static class Request {
		private String email;
	}

	@AllArgsConstructor
	@Getter
	public static class Response {
		private String message;
	}
}
