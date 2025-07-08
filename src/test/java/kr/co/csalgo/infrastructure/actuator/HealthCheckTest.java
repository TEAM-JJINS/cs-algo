package kr.co.csalgo.infrastructure.actuator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import redis.embedded.RedisServer;

@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckTest {

	private static RedisServer redisServer;

	@DynamicPropertySource
	static void overrideRedisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", () -> "localhost");
		registry.add("spring.redis.port", () -> 6380);
	}

	@BeforeAll
	static void startRedis() throws IOException {
		redisServer = new RedisServer(6380);
		redisServer.start();
	}

	@AfterAll
	static void stopRedis() throws IOException {
		if (redisServer != null && redisServer.isActive()) {
			redisServer.stop();
		}
	}

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("헬스체크는 OK를 반환한다.")
	void healthCheck() throws Exception {
		mockMvc.perform(get("/actuator/health"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("UP"));
	}
}
