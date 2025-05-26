package kr.co.csalgo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
			.components(new Components())
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("CS-ALGO Service API Docs")
			.description("CS-ALGO 서비스의 API 명세서로, 사용자 구독, 메일 송수신, 답변 피드백과 같은 주요 기능을 포함합니다.")
			.version("MVP 1.0.0");
	}
}
