package kr.co.csalgo.domain.question.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedbackResult {
	private String responseContent;
	private String questionSolution;

	@Builder
	public FeedbackResult(String responseContent, String questionSolution) {
		this.responseContent = responseContent;
		this.questionSolution = questionSolution;
	}
}

