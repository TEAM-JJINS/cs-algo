package kr.co.csalgo.infrastructure.feedback;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import kr.co.csalgo.infrastructure.similarity.LuceneSimilarityCalculator;

class SimpleFeedbackAnalyzerTest {

	private final SimilarityCalculator similarityCalculator = new LuceneSimilarityCalculator();
	private final SimpleFeedbackAnalyzer analyzer = new SimpleFeedbackAnalyzer(similarityCalculator);

	@Test
	@DisplayName("입력된 응답과 정답을 그대로 반환한다")
	void analyzeReturnsSameContent() {
		// given
		String responseContent = "사용자 답변 내용입니다.";
		String questionSolution = "정답 내용입니다.";

		// when
		FeedbackResult result = analyzer.analyze(responseContent, questionSolution);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getResponseContent()).isEqualTo(responseContent);
		assertThat(result.getQuestionSolution()).isEqualTo(questionSolution);
	}

	@Test
	@DisplayName("유사한 응답에 대해 유사도와 가이드를 반환한다")
	void analyze_shouldReturnFeedbackResult_withProperSimilarityAndGuide() {
		String questionSolution = "스프링은 자바 기반 프레임워크입니다";
		String userResponse = "스프링은 자바 웹 프레임워크야";

		FeedbackResult result = analyzer.analyze(userResponse, questionSolution);

		assertThat(result.getSimilarity()).isGreaterThan(30.0);
		assertThat(result.getGuideMessage()).isNotEmpty();
		assertThat(result.getQuestionSolution()).isEqualTo(questionSolution);
		assertThat(result.getResponseContent()).isEqualTo(userResponse);
	}
}
