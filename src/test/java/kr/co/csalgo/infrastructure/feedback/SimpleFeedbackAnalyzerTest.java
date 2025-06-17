package kr.co.csalgo.infrastructure.feedback;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.csalgo.domain.question.feedback.FeedbackResult;

class SimpleFeedbackAnalyzerTest {

	private final SimpleFeedbackAnalyzer analyzer = new SimpleFeedbackAnalyzer();

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
}
