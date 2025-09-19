package kr.co.csalgo.infrastructure.gpt;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import kr.co.csalgo.domain.ai.AiClient;
import kr.co.csalgo.infrastructure.gpt.config.GptConfig;
import kr.co.csalgo.infrastructure.gpt.dto.GptDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GptClient implements AiClient {

	private final RestTemplate restTemplate;
	private final GptConfig gptConfig;

	@Override
	public String ask(String prompt) {
		GptDto.Request request = new GptDto.Request(
			gptConfig.getModel(),
			List.of(new GptDto.Request.Message("user", prompt)),
			0.7
		);

		HttpEntity<GptDto.Request> entity = new HttpEntity<>(request);

		GptDto.Response response = restTemplate.postForObject(
			"https://api.openai.com/v1/chat/completions",
			entity,
			GptDto.Response.class
		);

		return response != null ? response.firstAnswer() : null;
	}
}
