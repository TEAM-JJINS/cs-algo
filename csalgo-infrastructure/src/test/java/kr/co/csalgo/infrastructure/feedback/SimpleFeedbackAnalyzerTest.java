package kr.co.csalgo.infrastructure.feedback;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import kr.co.csalgo.domain.similarity.SimilarityGuide;

class SimpleFeedbackAnalyzerTest {

	private SimilarityCalculator similarityCalculator;
	private SimpleFeedbackAnalyzer simpleAnalyzer;

	@BeforeEach
	void setUp() {
		similarityCalculator = mock(SimilarityCalculator.class);
		simpleAnalyzer = new SimpleFeedbackAnalyzer(similarityCalculator);
	}

	@Test
	void analyze_shouldReturnFeedbackResult_withSimilarityAndGuideMessage() {
		String questionTitle = "시간 복잡도란?";
		String userAnswer = "시간 복잡도는 연산 횟수를 뜻합니다.";
		String solution = "시간 복잡도는 입력 크기와 연산 횟수의 관계를 말합니다.";
		double expectedSimilarity = 0.75;

		when(similarityCalculator.calculate(solution, userAnswer))
			.thenReturn(expectedSimilarity);

		FeedbackResult result = simpleAnalyzer.analyze(questionTitle, userAnswer, solution);

		assertThat(result.getSimilarity()).isEqualTo(expectedSimilarity);
		assertThat(result.getSummary()).isEqualTo(SimilarityGuide.getGuideMessage(expectedSimilarity));
		assertThat(result.getStrengths()).isEmpty();
		assertThat(result.getImprovements()).isEmpty();
		assertThat(result.getLearningTips()).isEmpty();

		verify(similarityCalculator, times(1)).calculate(solution, userAnswer);
	}
}
