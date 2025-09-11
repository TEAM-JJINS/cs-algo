package kr.co.csalgo.infrastructure.similarity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;

public class EmbeddingSimilarityCalculator implements SimilarityCalculator {

	private static final String API_URL = "https://api-inference.huggingface.co/models/sentence-transformers/all-MiniLM-L6-v2";
	private final String apiToken;
	private final HttpClient client = HttpClient.newHttpClient();
	private final ObjectMapper mapper = new ObjectMapper();

	public EmbeddingSimilarityCalculator(@Value("${huggingface.api.token}") String apiToken) {
		this.apiToken = apiToken;
	}

	@Override
	public double calculate(String reference, String userInput) {
		try {
			Map<String, Object> body = Map.of(
				"inputs", Map.of(
					"source_sentence", reference,
					"sentences", List.of(userInput)
				)
			);

			String jsonBody = mapper.writeValueAsString(body);

			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(API_URL))
				.header("Authorization", "Bearer " + apiToken)
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonBody))
				.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new CustomBusinessException(ErrorCode.SIMILARITY_CALCULATION_ERROR);
			}

			// 응답은 [유사도 점수] 배열 형태
			List<Double> similarities = mapper.readValue(
				response.body(),
				mapper.getTypeFactory().constructCollectionType(List.class, Double.class)
			);

			// sentences가 하나뿐이므로 첫 번째 값 반환
			return similarities.getFirst();

		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.SIMILARITY_CALCULATION_ERROR);
		}
	}
}
