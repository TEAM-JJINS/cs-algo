package kr.co.csalgo.infrastructure.similarity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
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

	@Bean
	public SimilarityCalculator similarityCalculator() {
		switch (strategy.toLowerCase()) {
			case "tfidf":
				return new TfIdfSimilarityCalculator();
			case "embedding": {
				Dotenv dotenv = Dotenv.configure().directory("../").load();
				String token = dotenv.get("HUGGINGFACE_API_TOKEN");
				return new EmbeddingSimilarityCalculator(token);
			}
			case "hybrid": {
				Dotenv dotenv = Dotenv.configure().directory("../").load();
				String token = dotenv.get("HUGGINGFACE_API_TOKEN");
				return new HybridSimilarityCalculator(new TfIdfSimilarityCalculator(), new EmbeddingSimilarityCalculator(token), alpha);
			}
			default:
				return new CosineSimilarityCalculator();
		}
	}
}
