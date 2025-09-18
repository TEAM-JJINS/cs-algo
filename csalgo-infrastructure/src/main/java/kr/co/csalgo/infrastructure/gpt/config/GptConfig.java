package kr.co.csalgo.infrastructure.gpt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GptConfig {

	@Value("${openai.token}")
	private String secretKey;

	@Value("${openai.model}")
	private String model;

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	@Bean
	public HttpHeaders httpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + secretKey);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public String getModel() {
		return model;
	}
}
