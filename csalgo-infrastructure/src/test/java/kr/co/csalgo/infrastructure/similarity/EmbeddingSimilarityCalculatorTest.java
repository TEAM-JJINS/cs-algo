package kr.co.csalgo.infrastructure.similarity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.cdimascio.dotenv.Dotenv;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;

@Disabled
class EmbeddingSimilarityCalculatorTest {

	private SimilarityCalculator similarityCalculator;

	@BeforeEach
	void setUp() {
		Dotenv dotenv = Dotenv.configure().directory("../").load();
		String token = dotenv.get("HUGGINGFACE_API_TOKEN");
		similarityCalculator = new EmbeddingSimilarityCalculator(token);
	}

	@Test
	@DisplayName("같은 문장은 높은 유사도를 반환한다")
	void sameSentence() {
		// given
		String text1 = "나는 오늘 아침에 커피를 마셨다.";
		String text2 = "나는 오늘 아침에 커피를 마셨다.";

		// when
		double score = similarityCalculator.calculate(text1, text2);

		// then
		System.out.println("Similarity Score (sameSentence): " + score);
		assertThat(score).isGreaterThan(0.9); // 유사도는 0~1 범위
	}

	@Test
	@DisplayName("다른 문장은 낮은 유사도를 반환한다")
	void differentSentence() {
		// given
		String text1 = "로그인 성공";
		String text2 = "데이터베이스 오류";

		// when
		double score = similarityCalculator.calculate(text1, text2);

		// then
		System.out.println("Similarity Score (differentSentence): " + score);
		assertThat(score).isLessThan(0.8);
	}
}
