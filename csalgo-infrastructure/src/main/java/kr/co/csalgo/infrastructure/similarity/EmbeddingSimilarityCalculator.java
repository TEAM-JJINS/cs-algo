package kr.co.csalgo.infrastructure.similarity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.similarity.SimilarityCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EmbeddingSimilarityCalculator implements SimilarityCalculator {

	private static final String API_URL = "https://api-inference.huggingface.co/models/snunlp/KR-SBERT-V40K-klueNLI-augSTS";
	private final String apiToken;
	private final HttpClient client = HttpClient.newHttpClient();
	private final ObjectMapper mapper = new ObjectMapper();

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
			log.error("Embedding API raw response: status={}, body={}", response.statusCode(), response.body());

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

		} catch (IOException e) {
			throw new CustomBusinessException(ErrorCode.SIMILARITY_CALCULATION_ERROR);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CustomBusinessException(ErrorCode.SIMILARITY_CALCULATION_ERROR);
		}
	}
}
