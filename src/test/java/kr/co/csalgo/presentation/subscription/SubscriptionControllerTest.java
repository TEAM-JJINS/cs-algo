package kr.co.csalgo.presentation.subscription;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import kr.co.csalgo.application.user.dto.SubscriptionUseCaseDto;
import kr.co.csalgo.application.user.dto.UnsubscriptionUseCaseDto;
import kr.co.csalgo.application.user.usecase.SubscriptionUseCase;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.common.message.MessageCode;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SubscriptionControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private SubscriptionUseCase subscriptionUseCase;

	@Autowired
	private ObjectMapper mapper;

	@Test
	@DisplayName("사용자는 정상 이메일로 구독할 수 있다.")
	void createSuccess() throws Exception {
		SubscriptionUseCaseDto.Request request = SubscriptionUseCaseDto.Request.builder()
			.email("team-jjins@gmail.com")
			.build();

		mockMvc.perform(post("/api/subscriptions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("중복된 이메일로는 구독할 수 없다.")
	void duplicateEmail() throws Exception {
		SubscriptionUseCaseDto.Request request = SubscriptionUseCaseDto.Request.builder()
			.email("duplicate@gmail.com")
			.build();

		SubscriptionUseCaseDto.Response response = SubscriptionUseCaseDto.Response.builder()
			.message(MessageCode.SUBSCRIBE_SUCCESS.getMessage())
			.build();

		when(subscriptionUseCase.create(any()))
			.thenReturn(response)
			.thenThrow(new CustomBusinessException(ErrorCode.DUPLICATE_EMAIL));

		mockMvc.perform(post("/api/subscriptions")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(request)));

		mockMvc.perform(post("/api/subscriptions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isConflict())
			.andDo(print());
	}

	@Test
	@DisplayName("이메일이 공백이면 구독할 수 없다.")
	void blankEmail() throws Exception {
		SubscriptionUseCaseDto.Request request = SubscriptionUseCaseDto.Request.builder()
			.email("")
			.build();

		mockMvc.perform(post("/api/subscriptions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("올바르지 않은 이메일 형식으로는 구독할 수 없다.")
	void invalidateEmailFormat() throws Exception {
		SubscriptionUseCaseDto.Request request = SubscriptionUseCaseDto.Request.builder()
			.email("team-jjinsgmail.com")
			.build();

		mockMvc.perform(post("/api/subscriptions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("구독 해지 성공")
	void testUnsubscribeSuccess() throws Exception {
		// given
		UUID uuid = UUID.randomUUID();
		UnsubscriptionUseCaseDto.Request request = UnsubscriptionUseCaseDto.Request.builder()
			.userId(uuid)
			.build();
		UnsubscriptionUseCaseDto.Response response = UnsubscriptionUseCaseDto.Response.of();

		// when
		when(subscriptionUseCase.unsubscribe(request)).thenReturn(response);

		// then
		mockMvc.perform(delete("/api/subscriptions")
				.contentType(MediaType.APPLICATION_JSON)
				.param("userId", String.valueOf(request.getUserId())))
			.andExpect(status().isOk())
			.andDo(print());
	}
}
