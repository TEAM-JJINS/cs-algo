package kr.co.csalgo.server.question;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.application.problem.dto.QuestionDto;
import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.application.problem.usecase.GetQuestionUseCase;
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

	@MockitoBean
	private GetQuestionUseCase getQuestionUseCase;

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

	@Test
	@DisplayName("문제 목록 조회 성공 시 200 OK 반환")
	void getQuestionList() throws Exception {
		QuestionDto.Response q1 = QuestionDto.Response.builder()
			.title("문제1")
			.description("설명1")
			.solution("풀이1")
			.build();

		QuestionDto.Response q2 = QuestionDto.Response.builder()
			.title("문제2")
			.description("설명2")
			.solution("풀이2")
			.build();

		PageImpl<QuestionDto.Response> pageResult = new PageImpl<>(
			List.of(q1, q2),
			PageRequest.of(0, 2),
			10
		);

		when(getQuestionUseCase.getQuestionListWithPaging(0, 2)).thenReturn(pageResult);

		mockMvc.perform(get("/api/questions")
				.param("page", "0")
				.param("size", "2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.content.length()").value(2))
			.andExpect(jsonPath("$.content[0].title").value("문제1"))
			.andExpect(jsonPath("$.content[1].title").value("문제2"));
	}

}
