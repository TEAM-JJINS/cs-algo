package kr.co.csalgo.infrastructure.feedback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.csalgo.domain.ai.AiClient;
import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import kr.co.csalgo.infrastructure.feedback.GptFeedbackAnalyzer;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FeedbackAnalyzerConfig {
	private final SimilarityCalculator similarityCalculator;
	private final AiClient aiClient;

	@Bean
	public FeedbackAnalyzer feedbackAnalyzer() {
		return new GptFeedbackAnalyzer(similarityCalculator, aiClient);
	}
}
