package kr.co.csalgo.infrastructure.feedback;

import org.springframework.stereotype.Component;

import kr.co.csalgo.domain.ai.AiClient;
import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GptFeedbackAnalyzer implements FeedbackAnalyzer {

	private final SimilarityCalculator similarityCalculator;
	private final AiClient aiClient;

	@Override
	public FeedbackResult analyze(String title, String userAnswer, String solution) {
		// 유사도 점수 계산
		double similarity = similarityCalculator.calculate(solution, userAnswer);

		// GPT 호출
		return aiClient.ask(
			title,
			solution,
			userAnswer,
			similarity
		);
	}
}
