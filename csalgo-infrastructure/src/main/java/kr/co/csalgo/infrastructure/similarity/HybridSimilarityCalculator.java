package kr.co.csalgo.infrastructure.similarity;

import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HybridSimilarityCalculator implements SimilarityCalculator {

	private final SimilarityCalculator tfidfCalculator;
	private final SimilarityCalculator embeddingCalculator;
	private final double alpha; // 가중치 (0.0 ~ 1.0)

	@Override
	public double calculate(String reference, String userInput) {
		double tfidfScore = tfidfCalculator.calculate(reference, userInput);
		// 0~1 범위로 정규화
		double normalizedTfidf = tfidfScore / (tfidfScore + 1);

		double embeddingScore = embeddingCalculator.calculate(reference, userInput);

		return alpha * normalizedTfidf + (1 - alpha) * embeddingScore;
	}
}
