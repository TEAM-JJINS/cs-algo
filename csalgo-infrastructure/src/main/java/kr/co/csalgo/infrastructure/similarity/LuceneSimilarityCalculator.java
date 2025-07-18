package kr.co.csalgo.infrastructure.similarity;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;

public class LuceneSimilarityCalculator implements SimilarityCalculator {
	private final Analyzer analyzer = new KoreanAnalyzer();

	@Override
	public double calculate(String reference, String userInput) {
		return Optional.of(new TextPair(reference, userInput))
			.map(buildTermVectors)
			.map(pair -> computeCosine(pair.refMap(), pair.userMap()))
			.orElse(0.0) * 100.0;
	}

	private final Function<TextPair, TermVectorPair> buildTermVectors = (
		pair ->
			new TermVectorPair(
				getTermFrequencies(pair.reference()),
				getTermFrequencies(pair.userInput())
			)
	);

	private Map<String, Integer> getTermFrequencies(String text) {
		Map<String, Integer> freqMap = new HashMap<>();

		try (TokenStream stream = analyzer.tokenStream("content", text)) {
			CharTermAttribute termAttr = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				String term = termAttr.toString();
				freqMap.put(term, freqMap.getOrDefault(term, 0) + 1);
			}
			stream.end();
		} catch (IOException e) {
			throw new CustomBusinessException(ErrorCode.SIMILARITY_CALCULATION_ERROR);
		}
		return freqMap;
	}

	private double computeCosine(Map<String, Integer> vecA, Map<String, Integer> vecB) {
		Set<String> allTerms = new HashSet<>(vecA.keySet());
		allTerms.addAll(vecB.keySet());

		double dot = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for (String term : allTerms) {
			int freqRef = vecA.getOrDefault(term, 0);
			int freqUser = vecB.getOrDefault(term, 0);
			dot += freqRef * freqUser;
			normA += freqRef * freqRef;
			normB += freqUser * freqUser;
		}
		return (normA == 0 || normB == 0) ? 0.0 : dot / (Math.sqrt(normA) * Math.sqrt(normB));
	}

	private record TextPair(String reference, String userInput) {
	}

	private record TermVectorPair(Map<String, Integer> refMap, Map<String, Integer> userMap) {
	}
}
