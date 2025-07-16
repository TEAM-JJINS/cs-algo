package kr.co.csalgo.common.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import kr.co.csalgo.common.controller.TestExceptionController;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GlobalExceptionHandler.class, TestExceptionController.class})
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private GlobalExceptionHandler globalExceptionHandler;

	@Autowired
	private TestExceptionController testExceptionController;

	@BeforeEach
	void setup() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		this.mockMvc = MockMvcBuilders
			.standaloneSetup(testExceptionController)
			.setControllerAdvice(globalExceptionHandler)
			.setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
			.build();
	}

	@Test
	@DisplayName("비즈니스 예외 처리 - USER_NOT_FOUND")
	void testBusinessException() throws Exception {
		mockMvc.perform(get("/test/business"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()))
			.andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()))
			.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	@DisplayName("IllegalArgumentException 처리")
	void testIllegalArgumentException() throws Exception {
		mockMvc.perform(get("/test/illegal"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT.getCode()))
			.andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()))
			.andExpect(jsonPath("$.detail").exists());
	}

	@Test
	@DisplayName("Validation 실패 처리")
	void testValidationError() throws Exception {
		String invalidJson = """
			{ "email": "" }
			""";

		mockMvc.perform(post("/test/validation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT.getCode()))
			.andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()))
			.andExpect(jsonPath("$.detail").exists())
			.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	@DisplayName("JSON 파싱 실패 처리")
	void testMessageNotReadable() throws Exception {
		String malformedJson = "{ email: \"test@example.com\" }";

		mockMvc.perform(post("/test/validation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(malformedJson))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(ErrorCode.MESSAGE_NOT_READABLE.getCode()))
			.andExpect(jsonPath("$.message").value(ErrorCode.MESSAGE_NOT_READABLE.getMessage()))
			.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	@DisplayName("예상치 못한 예외 처리")
	void testUnexpectedException() throws Exception {
		mockMvc.perform(get("/test/unknown"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.code").value(ErrorCode.INTERNAL_SERVER_ERROR.getCode()))
			.andExpect(jsonPath("$.message").value(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()))
			.andExpect(jsonPath("$.detail").exists())
			.andExpect(jsonPath("$.timestamp").exists());
	}
}
