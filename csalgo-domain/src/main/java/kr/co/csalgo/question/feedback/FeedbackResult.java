package kr.co.csalgo.question.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedbackResult {
	private String responseContent;
	private String questionSolution;
	private double similarity;
	private String guideMessage;

	@Builder
	public FeedbackResult(String responseContent, String questionSolution, double similarity, String guideMessage) {
		this.responseContent = responseContent;
		this.questionSolution = questionSolution;
		this.similarity = similarity;
		this.guideMessage = guideMessage;
	}
}

