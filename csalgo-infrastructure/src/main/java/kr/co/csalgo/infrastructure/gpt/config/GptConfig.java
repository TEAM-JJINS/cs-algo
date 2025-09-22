package kr.co.csalgo.infrastructure.gpt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class GptConfig {

	@Value("${openai.token}")
	private String secretKey;

	@Value("${openai.model}")
	private String model;

	@Value("${openai.endpoint:https://api.openai.com/v1/chat/completions}")
	private String endpoint;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
			.defaultHeader("Authorization", "Bearer " + secretKey)
			.defaultHeader("Content-Type", "application/json")
			.build();
	}
}
