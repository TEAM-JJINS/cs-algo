package kr.co.csalgo.application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.csalgo.auth.type.VerificationCodeType;
import kr.co.csalgo.common.message.MessageCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailVerificationCodeDto {
	@Getter
	@NoArgsConstructor
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
	@NoArgsConstructor
	public static class Response {
		private String message;

		@Builder
		public Response(String message) {
			this.message = message;
		}

		public static Response of() {
			return Response.builder()
				.message(MessageCode.EMAIL_SENT_SUCCESS.getMessage())
				.build();
		}
	}

}
