package kr.co.csalgo.server.admin;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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

import kr.co.csalgo.application.admin.dto.AdminLoginDto;
import kr.co.csalgo.application.admin.dto.AdminRefreshDto;
import kr.co.csalgo.application.admin.dto.TokenPair;
import kr.co.csalgo.application.admin.usecase.AdminLoginUseCase;
import kr.co.csalgo.application.admin.usecase.AdminLogoutUseCase;
import kr.co.csalgo.application.admin.usecase.AdminRefreshTokenUseCase;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.common.message.CommonResponse;
import kr.co.csalgo.common.message.MessageCode;

@SpringBootTest
@AutoConfigureMockMvc
class AdminAuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockitoBean
	private AdminLoginUseCase adminLoginUseCase;

	@MockitoBean
	private AdminRefreshTokenUseCase adminRefreshTokenUseCase;

	@MockitoBean
	private AdminLogoutUseCase adminLogoutUseCase;

	@Test
	@DisplayName("관리자는 올바른 요청으로 로그인에 성공할 수 있다")
	void loginSuccess() throws Exception {
		AdminLoginDto.Request request = new AdminLoginDto.Request("admin@test.com", "1234");
		TokenPair tokenPair = new TokenPair("access-token", "refresh-token");

		when(adminLoginUseCase.login(any())).thenReturn(tokenPair);

		mockMvc.perform(post("/api/admin/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").value("access-token"))
			.andExpect(jsonPath("$.refreshToken").value("refresh-token"))
			.andDo(print());
	}

	@Test
	@DisplayName("잘못된 자격 증명으로 로그인 시 실패한다")
	void loginFail() throws Exception {
		AdminLoginDto.Request request = new AdminLoginDto.Request("admin@test.com", "wrong");

		when(adminLoginUseCase.login(any()))
			.thenThrow(new CustomBusinessException(ErrorCode.CREDENTIAL_NOT_FOUND));

		mockMvc.perform(post("/api/admin/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value(ErrorCode.CREDENTIAL_NOT_FOUND.getCode()))
			.andDo(print());
	}

	@Test
	@DisplayName("리프레시 토큰으로 액세스 토큰을 갱신할 수 있다")
	void refreshSuccess() throws Exception {
		AdminRefreshDto.Request request = new AdminRefreshDto.Request("refresh-token");
		TokenPair tokenPair = new TokenPair("new-access-token", "new-refresh-token");

		when(adminRefreshTokenUseCase.refresh(any())).thenReturn(tokenPair);

		mockMvc.perform(post("/api/admin/refresh")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").value("new-access-token"))
			.andExpect(jsonPath("$.refreshToken").value("new-refresh-token"))
			.andDo(print());
	}

	@Test
	@DisplayName("잘못된 리프레시 토큰이면 갱신에 실패한다")
	void refreshFail() throws Exception {
		AdminRefreshDto.Request request = new AdminRefreshDto.Request("invalid-refresh-token");

		when(adminRefreshTokenUseCase.refresh(any()))
			.thenThrow(new CustomBusinessException(ErrorCode.REFRESH_TOKEN_REUSE));

		mockMvc.perform(post("/api/admin/refresh")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(ErrorCode.REFRESH_TOKEN_REUSE.getCode()))
			.andDo(print());
	}

	@Test
	@DisplayName("관리자는 로그아웃을 성공적으로 수행한다.")
	void testLogoutSuccess() throws Exception {
		AdminRefreshDto.Request request = new AdminRefreshDto.Request("refresh-token");
		CommonResponse mockResponse = new CommonResponse(MessageCode.LOGOUT_SUCCESS.getMessage());

		when(adminLogoutUseCase.logout(any(AdminRefreshDto.Request.class)))
			.thenReturn(mockResponse);

		mockMvc.perform(post("/api/admin/logout")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(MessageCode.LOGOUT_SUCCESS.getMessage()));

		verify(adminLogoutUseCase, times(1)).logout(any(AdminRefreshDto.Request.class));
	}
}
