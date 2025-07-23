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
		private String solution;

		@Builder
		public Response(String title, String solution) {
			this.title = title;
			this.solution = solution;
		}

		public static Response of(Question question) {
			return Response.builder()
				.title(question.getTitle())
				.solution(question.getSolution())
				.build();
		}
	}
}
