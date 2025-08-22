package kr.co.csalgo.web.admin.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestonDto {
	@Getter
	@NoArgsConstructor
	public static class Response {
		private Long id;
		private String title;
		private String description;
		private String solution;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;

		@Builder
		public Response(Long id, String title, String description, String solution, LocalDateTime createdAt, LocalDateTime updatedAt) {
			this.id = id;
			this.title = title;
			this.description = description;
			this.solution = solution;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}
	}
}
