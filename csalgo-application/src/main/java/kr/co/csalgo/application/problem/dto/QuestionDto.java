package kr.co.csalgo.application.problem.dto;

import kr.co.csalgo.domain.question.entity.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionDto {
	@Getter
	public static class Response {
		private String title;
		private String description;
		private String solution;

		@Builder
		public Response(String title, String description, String solution) {
			this.title = title;
			this.description = description;
			this.solution = solution;
		}

		public static Response of(Question question) {
			return Response.builder()
				.title(question.getTitle())
				.description(question.getDescription())
				.solution(question.getSolution())
				.build();
		}
	}
}
