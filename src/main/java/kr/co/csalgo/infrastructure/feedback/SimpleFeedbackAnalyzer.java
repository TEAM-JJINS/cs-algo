package kr.co.csalgo.infrastructure.feedback;

import org.springframework.stereotype.Component;

import kr.co.csalgo.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.question.feedback.FeedbackResult;
import kr.co.csalgo.similarity.SimilarityCalculator;
import kr.co.csalgo.similarity.SimilarityGuide;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SimpleFeedbackAnalyzer implements FeedbackAnalyzer {
	private final SimilarityCalculator similarityCalculator;

	@Override
	public FeedbackResult analyze(String responseContent, String questionSolution) {
		double similarity = similarityCalculator.calculate(responseContent, questionSolution);
		String guideMessage = SimilarityGuide.getGuideMessage(similarity);

		return FeedbackResult.builder()
			.responseContent(responseContent)
			.questionSolution(questionSolution)
			.similarity(similarity)
			.guideMessage(guideMessage)
			.build();
	}
}
