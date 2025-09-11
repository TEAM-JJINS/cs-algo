package kr.co.csalgo.infrastructure.similarity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Disabled
class HybridSimilarityCalculatorTest {

	private TfIdfSimilarityCalculator tfidfCalculator;
	private EmbeddingSimilarityCalculator embeddingCalculator;

	@BeforeEach
	void setUp() {
		String token = "token";
		embeddingCalculator = new EmbeddingSimilarityCalculator(token);
		tfidfCalculator = new TfIdfSimilarityCalculator();
	}

	@Test
	@DisplayName("가중치 변화에 따라 Hybrid 점수가 달라진다 - 같은 문장")
	void weightComparison_sameSentence() {
		String ref = "오늘 날씨가 맑다";
		String input = "오늘 날씨가 맑다";

		HybridSimilarityCalculator tfidfOnly = new HybridSimilarityCalculator(tfidfCalculator, embeddingCalculator, 1.0);
		double tfidfScore = tfidfOnly.calculate(ref, input);

		HybridSimilarityCalculator embeddingOnly = new HybridSimilarityCalculator(tfidfCalculator, embeddingCalculator, 0.0);
		double embeddingScore = embeddingOnly.calculate(ref, input);

		HybridSimilarityCalculator hybrid = new HybridSimilarityCalculator(tfidfCalculator, embeddingCalculator, 0.5);
		double hybridScore = hybrid.calculate(ref, input);

		System.out.println("Same sentence - TF-IDF only: " + tfidfScore);
		System.out.println("Same sentence - Embedding only: " + embeddingScore);
		System.out.println("Same sentence - Hybrid (0.5): " + hybridScore);

		assertThat(hybridScore).isBetween(
			Math.min(tfidfScore, embeddingScore),
			Math.max(tfidfScore, embeddingScore)
		);
	}

	@Test
	@DisplayName("가중치 변화에 따라 Hybrid 점수가 달라진다 - 다른 문장")
	void weightComparison_differentSentence() {
		String ref = "오늘 날씨가 맑다";
		String input = "저는 어제 밥을 먹었습니다";

		HybridSimilarityCalculator tfidfOnly = new HybridSimilarityCalculator(tfidfCalculator, embeddingCalculator, 1.0);
		double tfidfScore = tfidfOnly.calculate(ref, input);

		HybridSimilarityCalculator embeddingOnly = new HybridSimilarityCalculator(tfidfCalculator, embeddingCalculator, 0.0);
		double embeddingScore = embeddingOnly.calculate(ref, input);

		HybridSimilarityCalculator hybrid = new HybridSimilarityCalculator(tfidfCalculator, embeddingCalculator, 0.5);
		double hybridScore = hybrid.calculate(ref, input);

		System.out.println("Different sentence - TF-IDF only: " + tfidfScore);
		System.out.println("Different sentence - Embedding only: " + embeddingScore);
		System.out.println("Different sentence - Hybrid (0.5): " + hybridScore);

		assertThat(hybridScore).isBetween(
			Math.min(tfidfScore, embeddingScore),
			Math.max(tfidfScore, embeddingScore)
		);
	}
}
