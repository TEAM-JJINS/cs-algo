package kr.co.csalgo.application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.co.csalgo.common.message.MessageCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SubscriptionUseCaseDto {
	@Getter
	@NoArgsConstructor
	public static class Request {
		@NotBlank(message = "이메일은 필수 입력 값입니다.")
		@Email(message = "이메일 형식이 올바르지 않습니다.")
		private String email;

		@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
		private String password;

		@Builder
		public Request(String email, String password) {
			this.email = email;
			this.password = password;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Response {
		private String message;

		@Builder
		public Response(String message) {
			this.message = message;
		}

		public static Response of() {
			return Response.builder()
				.message(MessageCode.SUBSCRIBE_SUCCESS.getMessage())
				.build();
		}
	}
}
