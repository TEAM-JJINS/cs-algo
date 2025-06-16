package kr.co.csalgo.infrastructure.feedback;

import org.springframework.stereotype.Component;

import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;

@Component
public class SimpleFeedbackAnalyzer implements FeedbackAnalyzer {
	@Override
	public FeedbackResult analyze(String responseContent, String questionSolution) {
		return FeedbackResult.builder()
			.responseContent(responseContent)
			.questionSolution(questionSolution)
			.build();
	}
}
