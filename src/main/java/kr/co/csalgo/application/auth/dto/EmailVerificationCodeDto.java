package kr.co.csalgo.application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.Builder;
import lombok.Getter;

public class EmailVerificationCodeDto {
	@Getter
	public static class Request {
		@NotBlank(message = "이메일은 필수 입력 값입니다.")
		@Email(message = "이메일 형식이 올바르지 않습니다.")
		private String email;

		@NotNull(message = "인증 유형은 필수 입력 값입니다.")
		private VerificationCodeType type;

		@Builder
		public Request(String email, VerificationCodeType type) {
			this.email = email;
			this.type = type;
		}
	}

	@Getter
	public static class Response {
		private String message;

		@Builder
		public Response(String message) {
			this.message = message;
		}

		public static Response of() {
			return Response.builder()
				.message("인증번호가 성공적으로 전송되었습니다.")
				.build();
		}
	}

}
