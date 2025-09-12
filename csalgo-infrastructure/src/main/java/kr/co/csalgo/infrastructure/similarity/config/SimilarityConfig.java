package kr.co.csalgo.infrastructure.similarity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import kr.co.csalgo.infrastructure.similarity.CosineSimilarityCalculator;
import kr.co.csalgo.infrastructure.similarity.EmbeddingSimilarityCalculator;
import kr.co.csalgo.infrastructure.similarity.HybridSimilarityCalculator;
import kr.co.csalgo.infrastructure.similarity.TfIdfSimilarityCalculator;

@Configuration
public class SimilarityConfig {

	@Value("${similarity.strategy:cosine}")
	private String strategy;

	@Value("${similarity.alpha:0.5}")
	private double alpha;

	@Value("${huggingface.api.token:test}")
	private String token;

	@Bean
	public SimilarityCalculator similarityCalculator() {
		return switch (strategy.toLowerCase()) {
			case "tfidf" -> new TfIdfSimilarityCalculator();
			case "embedding" -> new EmbeddingSimilarityCalculator(token);
			case "hybrid" -> new HybridSimilarityCalculator(new TfIdfSimilarityCalculator(), new EmbeddingSimilarityCalculator(token), alpha);
			default -> new CosineSimilarityCalculator();
		};
	}
}
