package kr.co.csalgo.application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailVerificationVerifyDto {
	@Getter
	@NoArgsConstructor
	public static class Request {
		@NotBlank(message = "이메일은 필수 입력 값입니다.")
		@Email(message = "이메일 형식이 올바르지 않습니다.")
		private String email;

		@NotNull(message = "인증 코드는 필수 입력 값입니다.")
		private String code;

		@NotNull(message = "인증 유형은 필수 입력 값입니다.")
		private VerificationCodeType type;

		@Builder
		public Request(String email, String code, VerificationCodeType type) {
			this.email = email;
			this.code = code;
			this.type = type;
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
				.message(MessageCode.EMAIL_VERIFICATION_SUCCESS.getMessage())
				.build();
		}
	}
}
