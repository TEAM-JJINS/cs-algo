package kr.co.csalgo.server.gpt;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import kr.co.csalgo.infrastructure.gpt.GptClient;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GptControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GptClient gptClient;

	@Test
	void testGptCallTest() throws Exception {
		// given
		when(gptClient.ask("안녕 GPT, 잘 연결됐어?"))
			.thenReturn("Mock 응답: 잘 연결됨!");

		// when + then
		mockMvc.perform(get("/gpt-test"))
			.andExpect(status().isOk())
			.andExpect(content().string("Mock 응답: 잘 연결됨!"));
	}
}
