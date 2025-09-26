package kr.co.csalgo.infrastructure.feedback;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.csalgo.domain.ai.AiClient;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;

class GptFeedbackAnalyzerTest {

	private final SimilarityCalculator similarityCalculator = mock(SimilarityCalculator.class);
	private final AiClient aiClient = mock(AiClient.class);

	private final GptFeedbackAnalyzer analyzer = new GptFeedbackAnalyzer(similarityCalculator, aiClient);

	@Test
	@DisplayName("유사도 계산 후 AiClient 결과를 그대로 반환한다")
	void analyze_shouldReturnFeedbackResult() {
		// given
		String title = "시간 복잡도와 공간 복잡도를 설명하시오.";
		String solution = "시간 복잡도는 입력 크기와 연산 횟수의 관계를 나타내며, 공간 복잡도는 추가 메모리 사용량을 의미한다.";
		String userAnswer = "시간 복잡도는 연산 횟수, 공간 복잡도는 메모리 크기와 관련있습니다.";

		double similarity = 82.5;
		when(similarityCalculator.calculate(solution, userAnswer))
			.thenReturn(similarity);

		FeedbackResult expected = FeedbackResult.builder()
			.similarity(similarity)
			.summary("답변은 전반적으로 유사도가 높은 편입니다. 다만 구체적 예시가 부족합니다.")
			.strengths(List.of("시간 복잡도를 올바르게 언급함", "공간 복잡도의 개념을 이해함"))
			.improvements(List.of("연산 횟수와 입력 크기의 상관관계 설명 필요", "메모리 증가 설명 보강 필요"))
			.learningTips(List.of("빅-O, 빅-Ω, 빅-Θ의 의미 학습", "정렬 알고리즘별 복잡도 비교"))
			.build();

		when(aiClient.ask(title, solution, userAnswer, similarity))
			.thenReturn(expected);

		// when
		FeedbackResult result = analyzer.analyze(title, userAnswer, solution);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getSimilarity()).isEqualTo(similarity);
		assertThat(result.getSummary()).isEqualTo(expected.getSummary());
		assertThat(result.getStrengths()).containsExactlyElementsOf(expected.getStrengths());
		assertThat(result.getImprovements()).containsExactlyElementsOf(expected.getImprovements());
		assertThat(result.getLearningTips()).containsExactlyElementsOf(expected.getLearningTips());

		verify(similarityCalculator).calculate(solution, userAnswer);
		verify(aiClient).ask(title, solution, userAnswer, similarity);
	}
}
