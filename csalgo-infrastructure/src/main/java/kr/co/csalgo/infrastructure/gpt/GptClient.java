package kr.co.csalgo.infrastructure.gpt;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.ai.AiClient;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import kr.co.csalgo.infrastructure.gpt.config.GptConfig;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GptClient implements AiClient {

	private final RestTemplate restTemplate;
	private final GptConfig gptConfig;
	private final FeedbackPromptProvider promptProvider;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public FeedbackResult ask(String question, String solution, String userAnswer, double similarityScore) {
		String systemPrompt = promptProvider.getSystemPrompt();
		String userPrompt = promptProvider.buildUserPrompt(question, solution, userAnswer, similarityScore);

		Map<String, Object> request = Map.of(
			"model", gptConfig.getModel(),
			"messages", new Object[] {
				Map.of("role", "system", "content", systemPrompt),
				Map.of("role", "user", "content", userPrompt)
			},
			"temperature", 0.7
		);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(gptConfig.getSecretKey());

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

		JsonNode response = restTemplate.postForObject(
			gptConfig.getEndpoint(), entity, JsonNode.class
		);

		try {
			String content = response.get("choices").get(0).get("message").get("content").asText();
			JsonNode json = objectMapper.readTree(content);

			return FeedbackResult.builder()
				.summary(json.get("summary").asText())
				.strengths(objectMapper.convertValue(json.get("strengths"),
					objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)))
				.improvements(objectMapper.convertValue(json.get("improvements"),
					objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)))
				.suggestions(objectMapper.convertValue(json.get("suggestions"),
					objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)))
				.learningTips(objectMapper.convertValue(json.get("learningTips"),
					objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)))
				.build();

		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.LLM_RESPONSE_PARSE_FAIL);
		}
	}
}
