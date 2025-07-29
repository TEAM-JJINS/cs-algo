package kr.co.csalgo.application.user.dto;

import java.util.UUID;

import kr.co.csalgo.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
	@Getter
	public static class Response {
		private String email;
		private UUID uuid;

		@Builder
		public Response(String email, UUID uuid) {
			this.email = email;
			this.uuid = uuid;
		}

		public static UserDto.Response of(User user) {
			return UserDto.Response.builder()
				.email(user.getEmail())
				.uuid(user.getUuid())
				.build();
		}
	}
}
