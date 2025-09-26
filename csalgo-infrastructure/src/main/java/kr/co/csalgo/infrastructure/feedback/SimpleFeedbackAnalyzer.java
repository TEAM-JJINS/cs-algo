package kr.co.csalgo.infrastructure.feedback;

import java.util.Collections;

import org.springframework.stereotype.Component;

import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import kr.co.csalgo.domain.similarity.SimilarityGuide;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SimpleFeedbackAnalyzer implements FeedbackAnalyzer {

	private final SimilarityCalculator similarityCalculator;

	@Override
	public FeedbackResult analyze(String questionTitle, String responseContent, String questionSolution) {
		double similarity = similarityCalculator.calculate(questionSolution, responseContent);
		String guideMessage = SimilarityGuide.getGuideMessage(similarity);

		return FeedbackResult.builder()
			.similarity(similarity)
			.summary(guideMessage)
			.strengths(Collections.emptyList())
			.improvements(Collections.emptyList())
			.learningTips(Collections.emptyList())
			.build();
	}
}

