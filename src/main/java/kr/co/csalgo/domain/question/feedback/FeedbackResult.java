package kr.co.csalgo.domain.question.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedbackResult {
	private String content;

	@Builder
	public FeedbackResult(String comment) {
		this.content = content;
	}
}

