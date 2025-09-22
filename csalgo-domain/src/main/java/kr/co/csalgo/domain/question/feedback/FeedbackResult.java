package kr.co.csalgo.domain.question.feedback;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedbackResult {
	private String summary;
	private List<String> strengths;
	private List<String> improvements;
	private List<String> suggestions;
	private List<String> learningTips;
	private double similarity;

	@Builder
	public FeedbackResult(String summary, List<String> strengths, List<String> improvements,
		List<String> suggestions, List<String> learningTips, double similarity) {
		this.summary = summary;
		this.strengths = strengths;
		this.improvements = improvements;
		this.suggestions = suggestions;
		this.learningTips = learningTips;
		this.similarity = similarity;
	}
}
