package kr.co.csalgo.application.user.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.csalgo.common.message.MessageCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UnsubscriptionUseCaseDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@Schema(name = "UnSubscriptionUseCaseDto.Request")
	public static class Request {
		@NotNull(message = "사용자 ID는 필수입니다.")
		private UUID userId;

		@Builder
		public Request(UUID userId) {
			this.userId = userId;
		}
	}

	@Getter
	@Schema(name = "UnSubscriptionUseCaseDto.Response")
	public static class Response {
		private final String message;

		@Builder
		public Response(String message) {
			this.message = message;
		}

		public static Response of() {
			return Response.builder()
				.message(MessageCode.UNSUBSCRIBE_SUCCESS.getMessage())
				.build();
		}
	}
}
