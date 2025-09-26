package kr.co.csalgo.server.gpt;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;

@SpringBootTest
@AutoConfigureMockMvc
class GptControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private FeedbackAnalyzer feedbackAnalyzer;

	@Test
	void testGptCallTest() throws Exception {
		FeedbackResult mockResult = FeedbackResult.builder()
			.summary("테스트 요약")
			.similarity(0.85)
			.build();

		when(feedbackAnalyzer.analyze(anyString(), anyString(), anyString()))
			.thenReturn(mockResult);

		mockMvc.perform(get("/gpt-test"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.summary").value("테스트 요약"))
			.andExpect(jsonPath("$.similarity").value(0.85));
	}

	@Test
	void testMailPreview() throws Exception {
		mockMvc.perform(get("/mail-preview").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
			.andExpect(content().string(org.hamcrest.Matchers.containsString("시간 복잡도와 공간 복잡도를 설명하시오")));
	}
}
