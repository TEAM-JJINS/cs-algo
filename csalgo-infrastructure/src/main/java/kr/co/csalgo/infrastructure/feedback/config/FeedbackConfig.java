package kr.co.csalgo.infrastructure.feedback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import kr.co.csalgo.infrastructure.feedback.FallbackFeedbackAnalyzer;
import kr.co.csalgo.infrastructure.feedback.SimpleFeedbackAnalyzer;

@Configuration
public class FeedbackConfig {

	@Bean
	public SimpleFeedbackAnalyzer simpleFeedbackAnalyzer(SimilarityCalculator similarityCalculator) {
		return new SimpleFeedbackAnalyzer(similarityCalculator);
	}

	@Bean
	public FeedbackAnalyzer feedbackAnalyzer(FallbackFeedbackAnalyzer fallback) {
		return fallback;
	}
}

