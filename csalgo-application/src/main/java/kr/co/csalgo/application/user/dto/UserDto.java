package kr.co.csalgo.application.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.type.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
	@Getter
	public static class Response {
		private Long id;
		private String email;
		private Role role;
		private UUID uuid;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;

		@Builder
		public Response(Long id, String email, Role role, UUID uuid, LocalDateTime createdAt, LocalDateTime updatedAt) {
			this.id = id;
			this.email = email;
			this.role = role;
			this.uuid = uuid;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}

		public static UserDto.Response of(User user) {
			return UserDto.Response.builder()
				.id(user.getId())
				.email(user.getEmail())
				.role(user.getRole())
				.uuid(user.getUuid())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
		}
	}
}
