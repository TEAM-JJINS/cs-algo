package kr.co.csalgo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.infrastructure.feedback.SimpleFeedbackAnalyzer;

@Configuration
public class FeedbackAnalyzerConfig {
	@Bean
	public FeedbackAnalyzer feedbackAnalyzer() {
		return new SimpleFeedbackAnalyzer();
	}
}

