package kr.co.csalgo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.csalgo.infrastructure.similarity.LuceneSimilarityCalculator;
import kr.co.csalgo.similarity.SimilarityCalculator;

@Configuration
public class SimilarityConfig {
	@Bean
	public SimilarityCalculator similarityCalculator() {
		return new LuceneSimilarityCalculator();
	}
}
