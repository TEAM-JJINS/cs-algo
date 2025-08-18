package kr.co.csalgo.application.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminLoginDto {
	@Getter
	@NoArgsConstructor
	public static class Request {
		@NotBlank(message = "이메일은 필수 입력 값입니다.")
		private String email;
		@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
		private String password;

		@Builder
		public Request(String email, String password) {
			this.email = email;
			this.password = password;
		}
	}
}
