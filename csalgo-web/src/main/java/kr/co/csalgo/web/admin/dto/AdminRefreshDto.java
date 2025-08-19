package kr.co.csalgo.web.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRefreshDto {
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Request {
		private String refreshToken;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Response {
		private String accessToken;
		private String refreshToken;
	}
}
