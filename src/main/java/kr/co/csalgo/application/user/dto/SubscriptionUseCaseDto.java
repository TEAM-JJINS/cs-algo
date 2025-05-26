package kr.co.csalgo.application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.co.csalgo.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

public class SubscriptionUseCaseDto {

	@Getter
	public static class Request {
		@NotBlank(message = "이메일은 필수 입력 값입니다.")
		@Email(message = "이메일 형식이 올바르지 않습니다.")
		private String email;

		@Builder
		public Request(String email) {
			this.email = email;
		}
	}

	@Getter
	public static class Response {
		private Long subscriptionId;

		@Builder
		public Response(Long subscriptionId) {
			this.subscriptionId = subscriptionId;
		}

		public static Response fromEntity(User user) {
			return Response.builder()
				.subscriptionId(user.getId())
				.build();
		}
	}
}
