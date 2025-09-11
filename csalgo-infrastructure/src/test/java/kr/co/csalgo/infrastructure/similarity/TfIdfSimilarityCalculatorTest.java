package kr.co.csalgo.infrastructure.similarity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TfIdfSimilarityCalculatorTest {

	private final TfIdfSimilarityCalculator calculator = new TfIdfSimilarityCalculator();

	@Test
	@DisplayName("동일 문장은 높은 점수를 반환한다")
	void sameSentence() {
		double score = calculator.calculate("로그인 성공", "로그인 성공");
		System.out.println("Score = " + score);
		assertThat(score).isGreaterThan(70.0);
	}

	@Test
	@DisplayName("다른 문장은 낮은 점수를 반환한다")
	void differentSentence() {
		double score = calculator.calculate("로그인 성공", "데이터베이스 오류");
		System.out.println("Score = " + score);
		assertThat(score).isLessThan(20.0);
	}

	@Test
	@DisplayName("부분 일치 문장은 중간 점수를 반환한다")
	void partialOverlap() {
		double score = calculator.calculate("로그인 실패", "로그인 오류");
		System.out.println("Score = " + score);
		assertThat(score).isBetween(20.0, 80.0);
	}
}
