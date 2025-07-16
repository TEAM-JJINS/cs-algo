package kr.co.csalgo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import kr.co.csalgo.infrastructure.similarity.LuceneSimilarityCalculator;

@Configuration
public class SimilarityConfig {
	@Bean
	public SimilarityCalculator similarityCalculator() {
		return new LuceneSimilarityCalculator();
	}
}
