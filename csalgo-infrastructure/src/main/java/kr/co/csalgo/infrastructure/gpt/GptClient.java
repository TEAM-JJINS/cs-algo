package kr.co.csalgo.infrastructure.gpt;

import java.util.List;

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
import kr.co.csalgo.infrastructure.gpt.dto.GptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GptClient implements AiClient {

	private final RestTemplate restTemplate;
	private final GptConfig gptConfig;
	private final FeedbackPromptProvider promptProvider;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public FeedbackResult ask(String question, String solution, String userAnswer, double similarityScore) {
		String systemPrompt = promptProvider.getSystemPrompt();
		String userPrompt = promptProvider.buildUserPrompt(question, solution, userAnswer, similarityScore);

		GptDto.Request request = buildRequest(systemPrompt, userPrompt);
		GptDto.Response response = callOpenAiApi(request);

		return parseResponse(response, similarityScore);
	}

	private GptDto.Request buildRequest(String systemPrompt, String userPrompt) {
		return GptDto.Request.builder()
			.model(gptConfig.getModel())
			.temperature(0.7)
			.messages(List.of(
				new GptDto.Request.Message("system", systemPrompt),
				new GptDto.Request.Message("user", userPrompt)
			))
			.build();
	}

	private GptDto.Response callOpenAiApi(GptDto.Request request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(gptConfig.getSecretKey());

		HttpEntity<GptDto.Request> entity = new HttpEntity<>(request, headers);

		return restTemplate.postForObject(
			gptConfig.getEndpoint(),
			entity,
			GptDto.Response.class
		);
	}

	private FeedbackResult parseResponse(GptDto.Response response, double similarityScore) {
		try {
			String content = response.getChoices().get(0).getMessage().getContent();
			JsonNode json = objectMapper.readTree(content);

			return FeedbackResult.builder()
				.similarity(similarityScore)
				.summary(json.get("summary").asText())
				.strengths(objectMapper.convertValue(json.get("strengths"),
					objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)))
				.improvements(objectMapper.convertValue(json.get("improvements"),
					objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)))
				.learningTips(objectMapper.convertValue(json.get("learningTips"),
					objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)))
				.build();

		} catch (Exception e) {
			log.error("LLM 응답 파싱 실패", e);
			throw new CustomBusinessException(ErrorCode.LLM_RESPONSE_PARSE_FAIL);
		}
	}
}
