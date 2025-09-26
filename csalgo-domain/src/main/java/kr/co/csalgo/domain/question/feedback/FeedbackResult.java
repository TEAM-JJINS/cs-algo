package kr.co.csalgo.domain.question.feedback;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedbackResult {
	private double similarity;
	private String summary;
	private List<String> strengths;
	private List<String> improvements;
	private List<String> learningTips;

	@Builder
	public FeedbackResult(double similarity, String summary, List<String> strengths, List<String> improvements, List<String> learningTips) {
		this.similarity = similarity;
		this.summary = summary;
		this.strengths = strengths;
		this.improvements = improvements;
		this.learningTips = learningTips;
	}
}
