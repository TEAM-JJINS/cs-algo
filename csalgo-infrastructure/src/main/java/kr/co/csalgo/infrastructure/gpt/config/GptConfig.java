package kr.co.csalgo.infrastructure.gpt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GptConfig {

	@Value("${openai.token}")
	private String secretKey;

	@Value("${openai.model}")
	private String model;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
			.defaultHeader("Authorization", "Bearer " + secretKey)
			.defaultHeader("Content-Type", "application/json")
			.build();
	}

	public String getModel() {
		return model;
	}
}
