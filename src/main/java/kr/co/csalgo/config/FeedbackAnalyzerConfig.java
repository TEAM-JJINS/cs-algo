package kr.co.csalgo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.csalgo.infrastructure.feedback.SimpleFeedbackAnalyzer;
import kr.co.csalgo.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.similarity.SimilarityCalculator;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FeedbackAnalyzerConfig {
	private final SimilarityCalculator similarityCalculator;

	@Bean
	public FeedbackAnalyzer feedbackAnalyzer() {
		return new SimpleFeedbackAnalyzer(similarityCalculator);
	}
}
