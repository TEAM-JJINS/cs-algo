package kr.co.csalgo.domain.similarity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimilarityGuideTest {

	@Test
	@DisplayName("90~100 점수는 EXCELLENT 가이드를 반환한다")
	void getGuideMessage_Excellent() {
		assertThat(SimilarityGuide.getGuideMessage(90)).isEqualTo(SimilarityGuide.EXCELLENT.getMessage());
		assertThat(SimilarityGuide.getGuideMessage(100)).isEqualTo(SimilarityGuide.EXCELLENT.getMessage());
	}

	@Test
	@DisplayName("70~89 점수는 GREAT 가이드를 반환한다")
	void getGuideMessage_Great() {
		assertThat(SimilarityGuide.getGuideMessage(70)).isEqualTo(SimilarityGuide.GREAT.getMessage());
		assertThat(SimilarityGuide.getGuideMessage(89)).isEqualTo(SimilarityGuide.GREAT.getMessage());
	}

	@Test
	@DisplayName("50~69 점수는 GOOD 가이드를 반환한다")
	void getGuideMessage_Good() {
		assertThat(SimilarityGuide.getGuideMessage(50)).isEqualTo(SimilarityGuide.GOOD.getMessage());
		assertThat(SimilarityGuide.getGuideMessage(69)).isEqualTo(SimilarityGuide.GOOD.getMessage());
	}

	@Test
	@DisplayName("30~49 점수는 FAIR 가이드를 반환한다")
	void getGuideMessage_Fair() {
		assertThat(SimilarityGuide.getGuideMessage(30)).isEqualTo(SimilarityGuide.FAIR.getMessage());
		assertThat(SimilarityGuide.getGuideMessage(49)).isEqualTo(SimilarityGuide.FAIR.getMessage());
	}

	@Test
	@DisplayName("0~29 점수 및 예외 범위는 POOR 가이드를 반환한다")
	void getGuideMessage_Poor() {
		assertThat(SimilarityGuide.getGuideMessage(0)).isEqualTo(SimilarityGuide.POOR.getMessage());
		assertThat(SimilarityGuide.getGuideMessage(29)).isEqualTo(SimilarityGuide.POOR.getMessage());
		assertThat(SimilarityGuide.getGuideMessage(-5)).isEqualTo(SimilarityGuide.POOR.getMessage());
	}

	@Test
	@DisplayName("소수점 점수도 올림 반영하여 가이드 반환")
	void getGuideMessage_RoundUp() {
		assertThat(SimilarityGuide.getGuideMessage(89.6)).isEqualTo(SimilarityGuide.EXCELLENT.getMessage());
		assertThat(SimilarityGuide.getGuideMessage(69.7)).isEqualTo(SimilarityGuide.GREAT.getMessage());
	}
}
