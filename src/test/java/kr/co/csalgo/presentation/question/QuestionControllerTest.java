package kr.co.csalgo.presentation.question;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;
import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.application.problem.usecase.SendQuestionMailUseCase;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.common.message.MessageCode;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class QuestionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private SendQuestionMailUseCase sendQuestionMailUseCase;

	private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Test
	@DisplayName("특정 사용자에게 문제 메일 전송 성공 시 200 OK를 반환한다")
	void sendToSingleUserSuccess() throws Exception {
		SendQuestionMailDto.Request request = SendQuestionMailDto.Request.builder()
			.questionId(1L)
			.userId(28L)
			.build();

		when(sendQuestionMailUseCase.execute(any()))
			.thenReturn(SendQuestionMailDto.Response.of());

		mockMvc.perform(post("/api/question/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(MessageCode.SEND_QUESTION_MAIL_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("전체 사용자에게 문제 메일 전송 성공 시 200 OK를 반환한다")
	void sendToAllUsersSuccess() throws Exception {
		SendQuestionMailDto.Request request = SendQuestionMailDto.Request.builder()
			.questionId(1L)
			.build(); // userId 없음 → 전체 발송

		when(sendQuestionMailUseCase.execute(any()))
			.thenReturn(SendQuestionMailDto.Response.of());

		mockMvc.perform(post("/api/question/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(MessageCode.SEND_QUESTION_MAIL_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("존재하지 않는 질문으로 요청 시 404 Not Found를 반환한다")
	void sendQuestionNotFound() throws Exception {
		SendQuestionMailDto.Request request = SendQuestionMailDto.Request.builder()
			.questionId(999L)
			.userId(28L)
			.build();

		when(sendQuestionMailUseCase.execute(any()))
			.thenThrow(new CustomBusinessException(ErrorCode.QUESTION_NOT_FOUND));

		mockMvc.perform(post("/api/question/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(ErrorCode.QUESTION_NOT_FOUND.getMessage()));
	}

	@Test
	@DisplayName("이메일 전송 요청 시 questionId가 없으면 400 Bad Request를 반환한다")
	void missingQuestionId() throws Exception {
		String invalidJson = "{ 'userId': 28 }";

		mockMvc.perform(post("/api/question/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
			.andExpect(status().isBadRequest());
	}
}
