package kr.co.csalgo.web.admin.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {
	@Getter
	@NoArgsConstructor
	public static class Request {
		private int id;

		@Builder
		public Request(int id) {
			this.id = id;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Response {
		private int id;
		private String email;
		private String role;
		private String uuid;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;

		@Builder
		public Response(int id, String email, String role, String uuid, LocalDateTime createdAt, LocalDateTime updatedAt) {
			this.id = id;
			this.email = email;
			this.role = role;
			this.uuid = uuid;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}
	}
}
