package kr.co.csalgo.server.question;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.application.problem.usecase.SendQuestionMailUseCase;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.common.message.MessageCode;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QuestionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private SendQuestionMailUseCase sendQuestionMailUseCase;

	@Autowired
	private ObjectMapper mapper;

	@Test
	@DisplayName("단일 사용자에게 문제 전송 성공 시 200 OK 반환")
	void sendQuestionToSingleUser() throws Exception {
		when(sendQuestionMailUseCase.execute(any()))
			.thenReturn(SendQuestionMailDto.Response.of());

		String body = mapper.writeValueAsString(
			SendQuestionMailDto.Request.builder()
				.userId(28L)
				.build());

		mockMvc.perform(post("/api/questions/1/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message")
				.value(MessageCode.SEND_QUESTION_MAIL_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("scheduledTime 포함 시에도 성공적으로 전송")
	void sendQuestionWithScheduledTime() throws Exception {
		when(sendQuestionMailUseCase.execute(any()))
			.thenReturn(SendQuestionMailDto.Response.of());

		String futureTime = LocalDateTime.now()
			.plusHours(1)
			.format(DateTimeFormatter.ISO_DATE_TIME);

		String body = mapper.writeValueAsString(
			SendQuestionMailDto.Request.builder()
				.userId(28L)
				.scheduledTime(LocalDateTime.parse(futureTime))
				.build());

		mockMvc.perform(post("/api/questions/1/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message")
				.value(MessageCode.SEND_QUESTION_MAIL_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("존재하지 않는 문제로 요청 시 404 반환")
	void sendQuestionNotFound() throws Exception {
		when(sendQuestionMailUseCase.execute(any()))
			.thenThrow(new CustomBusinessException(ErrorCode.QUESTION_NOT_FOUND));

		String body = mapper.writeValueAsString(
			SendQuestionMailDto.Request.builder()
				.userId(28L)
				.build());

		mockMvc.perform(post("/api/questions/999/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message")
				.value(ErrorCode.QUESTION_NOT_FOUND.getMessage()));
	}
}
