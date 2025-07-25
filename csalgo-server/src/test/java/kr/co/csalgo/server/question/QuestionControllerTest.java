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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.application.problem.dto.QuestionDto;
import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.application.problem.usecase.DeleteQuestionUseCase;
import kr.co.csalgo.application.problem.usecase.GetQuestionUseCase;
import kr.co.csalgo.application.problem.usecase.SendQuestionMailUseCase;
import kr.co.csalgo.application.problem.usecase.UpdateQuestionUseCase;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.common.message.CommonResponse;
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

	@MockitoBean
	private UpdateQuestionUseCase updateQuestionUseCase;

	@MockitoBean
	private DeleteQuestionUseCase deleteQuestionUseCase;

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
	void testGetQuestionListSuccess() throws Exception {
		QuestionDto.Response q1 = QuestionDto.Response.builder()
			.title("문제1")
			.solution("풀이1")
			.build();

		QuestionDto.Response q2 = QuestionDto.Response.builder()
			.title("문제2")
			.solution("풀이2")
			.build();

		List<QuestionDto.Response> result = List.of(q1, q2);
		when(getQuestionUseCase.getQuestionListWithPaging(1, 2)).thenReturn(result);

		mockMvc.perform(get("/api/questions")
				.param("page", "1")
				.param("size", "2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].title").value("문제1"))
			.andExpect(jsonPath("$[1].title").value("문제2"));
	}

	@Test
	@DisplayName("문제 상세 조회 성공 시 200 OK 반환")
	void testGetQuestionDetailSuccess() throws Exception {
		QuestionDto.Response question = QuestionDto.Response.builder()
			.title("문제")
			.solution("풀이")
			.build();

		when(getQuestionUseCase.getQuestionDetail(1L)).thenReturn(question);

		mockMvc.perform(get("/api/questions/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("문제"))
			.andExpect(jsonPath("$.solution").value("풀이"));
	}

	@Test
	@DisplayName("문제 수정 성공 시 200 OK와 메시지 반환")
	void testUpdateQuestionSuccess() throws Exception {
		Long questionId = 1L;
		String body = mapper.writeValueAsString(
			QuestionDto.Request.builder()
				.title("수정된 제목")
				.solution("수정된 풀이")
				.build()
		);
		when(updateQuestionUseCase.updateQuestion(eq(questionId), any(QuestionDto.Request.class)))
			.thenReturn(MessageCode.UPDATE_QUESTION_SUCCESS.getMessage());

		mockMvc.perform(put("/api/questions/{questionId}", questionId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isOk())
			.andExpect(content().string(MessageCode.UPDATE_QUESTION_SUCCESS.getMessage()));

	}

	@Test
	@DisplayName("문제 삭제 성공 시 200 OK와 메시지 반환")
	void testDeleteQuestionSuccess() throws Exception {
		Long questionId = 1L;

		when(deleteQuestionUseCase.deleteQuestion(eq(questionId)))
			.thenReturn(new CommonResponse(MessageCode.DELETE_QUESTION_SUCCESS.getMessage()));

		mockMvc.perform(delete("/api/questions/{questionId}", questionId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(MessageCode.DELETE_QUESTION_SUCCESS.getMessage()));
	}

}
