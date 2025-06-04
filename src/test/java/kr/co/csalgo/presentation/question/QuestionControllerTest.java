package kr.co.csalgo.presentation.question;

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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
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

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	@DisplayName("단일 사용자에게 문제 전송 성공 시 200 OK 반환")
	void sendQuestionToSingleUser() throws Exception {
		when(sendQuestionMailUseCase.execute(any()))
			.thenReturn(SendQuestionMailDto.Response.of());

		mockMvc.perform(post("/api/questions/1/send")
				.param("userId", "28"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(MessageCode.SEND_QUESTION_MAIL_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("전체 사용자에게 문제 전송 성공 시 200 OK 반환")
	void sendQuestionToAllUsers() throws Exception {
		when(sendQuestionMailUseCase.execute(any()))
			.thenReturn(SendQuestionMailDto.Response.of());

		mockMvc.perform(post("/api/questions/1/send"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(MessageCode.SEND_QUESTION_MAIL_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("scheduledTime 파라미터를 포함해도 성공적으로 전송됨")
	void sendQuestionWithScheduledTime() throws Exception {
		when(sendQuestionMailUseCase.execute(any()))
			.thenReturn(SendQuestionMailDto.Response.of());

		String futureTime = LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ISO_DATE_TIME);

		mockMvc.perform(post("/api/questions/1/send")
				.param("scheduledTime", futureTime))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(MessageCode.SEND_QUESTION_MAIL_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("존재하지 않는 문제로 요청 시 404 반환")
	void sendQuestionNotFound() throws Exception {
		when(sendQuestionMailUseCase.execute(any()))
			.thenThrow(new CustomBusinessException(ErrorCode.QUESTION_NOT_FOUND));

		mockMvc.perform(post("/api/questions/999/send")
				.param("userId", "28"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(ErrorCode.QUESTION_NOT_FOUND.getMessage()));
	}
}
