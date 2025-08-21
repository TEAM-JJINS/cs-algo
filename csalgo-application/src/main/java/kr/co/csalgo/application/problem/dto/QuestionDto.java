package kr.co.csalgo.application.problem.dto;

import java.time.LocalDateTime;

import kr.co.csalgo.domain.question.entity.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionDto {
	@Getter
	@Setter
	@NoArgsConstructor
	public static class Request {
		private String title;
		private String description;
		private String solution;

		@Builder
		public Request(String title, String description, String solution) {
			this.title = title;
			this.description = description;
			this.solution = solution;
		}
	}

	@Getter
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

		public static Response of(Question question) {
			return Response.builder()
				.id(question.getId())
				.title(question.getTitle())
				.description(question.getDescription())
				.solution(question.getSolution())
				.createdAt(question.getCreatedAt())
				.updatedAt(question.getUpdatedAt())
				.build();
		}
	}
}
