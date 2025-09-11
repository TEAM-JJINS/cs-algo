package kr.co.csalgo.infrastructure.similarity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.csalgo.domain.similarity.SimilarityCalculator;

class LuceneSimilarityCalculatorTest {

	private final SimilarityCalculator calculator = new CosineSimilarityCalculator();

	@Test
	@DisplayName("유사도 계산 - 유사한 텍스트에 대해 높은 유사도 반환")
	void calculate_shouldReturnHighSimilarity_forSimilarTexts() {
		String reference = "나는 학교에 갑니다";
		String userInput = "나는 학교에 갔어요";

		double similarity = calculator.calculate(reference, userInput);

		assertThat(similarity).isGreaterThan(50.0);
	}

	@Test
	@DisplayName("유사도 계산 - 유사하지 않은 텍스트에 대해 낮은 유사도 반환")
	void calculate_shouldReturnLowSimilarity_forDifferentTexts() {
		String reference = "나는 학교에 갑니다";
		String userInput = "저는 집에서 밥을 먹었어요";

		double similarity = calculator.calculate(reference, userInput);

		assertThat(similarity).isLessThan(30.0);
	}
}
