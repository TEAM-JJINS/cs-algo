package kr.co.csalgo.infrastructure.similarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.search.similarities.ClassicSimilarity;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;

public class TfIdfSimilarityCalculator implements SimilarityCalculator {

	private final Analyzer analyzer = new KoreanAnalyzer();
	private final ClassicSimilarity similarity = new ClassicSimilarity();

	@Override
	public double calculate(String reference, String userInput) {
		try {
			// 1. 토큰화
			List<String> refTokens = tokenize(reference);
			List<String> userTokens = tokenize(userInput);

			if (refTokens.isEmpty() || userTokens.isEmpty()) {
				return 0.0;
			}

			// 2. 전체 코퍼스 (reference + userInput)
			List<List<String>> documents = List.of(refTokens, userTokens);

			// 3. DF (Document Frequency) 계산
			Map<String, Integer> df = new HashMap<>();
			for (List<String> doc : documents) {
				Set<String> uniqueTerms = new HashSet<>(doc);
				for (String term : uniqueTerms) {
					df.put(term, df.getOrDefault(term, 0) + 1);
				}
			}

			int docCount = documents.size();

			// 4. TF-IDF 벡터 계산
			Map<String, Float> refVector = buildTfIdfVector(refTokens, df, docCount);
			Map<String, Float> userVector = buildTfIdfVector(userTokens, df, docCount);

			// 5. 코사인 유사도
			return computeCosine(refVector, userVector) * 100.0;
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.SIMILARITY_CALCULATION_ERROR);
		}
	}

	private List<String> tokenize(String text) throws IOException {
		List<String> tokens = new ArrayList<>();
		try (TokenStream stream = analyzer.tokenStream("content", text)) {
			CharTermAttribute termAttr = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				tokens.add(termAttr.toString());
			}
			stream.end();
		}
		return tokens;
	}

	private Map<String, Float> buildTfIdfVector(List<String> tokens, Map<String, Integer> df, int docCount) {
		Map<String, Integer> tf = new HashMap<>();
		for (String token : tokens) {
			tf.put(token, tf.getOrDefault(token, 0) + 1);
		}

		Map<String, Float> tfIdf = new HashMap<>();
		for (Map.Entry<String, Integer> entry : tf.entrySet()) {
			String term = entry.getKey();
			int freq = entry.getValue();
			int docFreq = df.getOrDefault(term, 1);

			float tfWeight = similarity.tf(freq);
			float idfWeight = similarity.idf(docFreq, docCount);

			tfIdf.put(term, tfWeight * idfWeight);
		}
		return tfIdf;
	}

	private double computeCosine(Map<String, Float> vecA, Map<String, Float> vecB) {
		Set<String> allTerms = new HashSet<>();
		allTerms.addAll(vecA.keySet());
		allTerms.addAll(vecB.keySet());

		double dotProduct = 0.0;
		double normRef = 0.0;
		double normUser = 0.0;

		for (String term : allTerms) {
			double refWeight = vecA.getOrDefault(term, 0.0f);
			double userWeight = vecB.getOrDefault(term, 0.0f);

			dotProduct += refWeight * userWeight;
			normRef += refWeight * refWeight;
			normUser += userWeight * userWeight;
		}

		return (normRef == 0 || normUser == 0)
			? 0.0
			: dotProduct / (Math.sqrt(normRef) * Math.sqrt(normUser));
	}
}
