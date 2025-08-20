package kr.co.csalgo.server.user;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.application.common.dto.PagedResponse;
import kr.co.csalgo.application.user.dto.UserDto;
import kr.co.csalgo.application.user.usecase.DeleteUserUseCase;
import kr.co.csalgo.application.user.usecase.GetUserUseCase;
import kr.co.csalgo.common.message.CommonResponse;
import kr.co.csalgo.common.message.MessageCode;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin@csalgo.co.kr", roles = {"ADMIN"})
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GetUserUseCase getUserUseCase;

	@MockitoBean
	private DeleteUserUseCase deleteUserUseCase;

	@Autowired
	private ObjectMapper mapper;

	@Test
	@DisplayName("사용자 목록 조회 성공 시 200 OK 반환")
	void testGetUserListSuccess() throws Exception {
		PagedResponse<UserDto.Response> pagedResponse = PagedResponse.<UserDto.Response>builder()
			.content(List.of(
					UserDto.Response.builder()
						.email("user1@example.com")
						.uuid(UUID.randomUUID())
						.build(),
					UserDto.Response.builder()
						.email("user2@example.com")
						.uuid(UUID.randomUUID())
						.build()
				)
			)
			.currentPage(1)
			.totalPages(1)
			.totalElements(2)
			.first(true)
			.last(true)
			.build();

		when(getUserUseCase.getUserListWithPaging(1, 2)).thenReturn(pagedResponse);

		mockMvc.perform(get("/api/users")
				.param("page", "1")
				.param("size", "2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].email").value("user1@example.com"))
			.andExpect(jsonPath("$[1].email").value("user2@example.com"));
	}

	@Test
	@DisplayName("사용자 상세 조회 성공 시 200 OK 반환")
	void testGetUserDetailSuccess() throws Exception {
		UserDto.Response user = UserDto.Response.builder()
			.email("user1@example.com")
			.uuid(UUID.randomUUID())
			.build();

		when(getUserUseCase.getUserDetail(1L)).thenReturn(user);

		mockMvc.perform(get("/api/users/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email").value("user1@example.com"));
	}

	@Test
	@DisplayName("사용자 삭제 성공 시 200 OK 반환")
	void testDeleteUserSuccess() throws Exception {
		Long userId = 1L;

		when(deleteUserUseCase.deleteUser(userId)).thenReturn(new CommonResponse(MessageCode.DELETE_USER_SUCCESS.getMessage()));

		mockMvc.perform(delete("/api/users/{userId}", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(MessageCode.DELETE_USER_SUCCESS.getMessage()));
	}

}
