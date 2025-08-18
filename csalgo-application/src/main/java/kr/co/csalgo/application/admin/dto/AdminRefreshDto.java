package kr.co.csalgo.application.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRefreshDto {
	@Getter
	@NoArgsConstructor
	public static class Request {
		@NotBlank(message = "리프레시 토큰은 필수 입력 값입니다.")
		private String refreshToken;

		@Builder
		public Request(String refreshToken) {
			this.refreshToken = refreshToken;
		}
	}
}
