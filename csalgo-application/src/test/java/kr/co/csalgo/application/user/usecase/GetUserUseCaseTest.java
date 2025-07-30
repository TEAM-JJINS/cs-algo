package kr.co.csalgo.application.user.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.application.user.dto.UserDto;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;

@DisplayName("GetUserUseCase 테스트")
@ExtendWith(MockitoExtension.class)
public class GetUserUseCaseTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private GetUserUseCase getUserUseCase;

	@Test
	@DisplayName("사용자 리스트 페이징 조회 테스트 성공")
	void testGetUserListWithPagingSuccess() {
		List<User> users = List.of(
			User.builder().email("user1@example.com").build(),
			User.builder().email("user2@example.com").build()
		);
		when(userService.list(0, 2)).thenReturn(users);

		List<UserDto.Response> result = getUserUseCase.getUserListWithPaging(1, 2);
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getEmail()).isEqualTo("user1@example.com");
		assertThat(result.get(1).getEmail()).isEqualTo("user2@example.com");
	}

	@Test
	@DisplayName("사용자 상세 조회 테스트 성공")
	void testGetUserDetailSuccess() {
		User user = User.builder().email("user1@example.com").build();
		when(userService.read(1L)).thenReturn(user);

		UserDto.Response result = getUserUseCase.getUserDetail(1L);
		assertThat(result.getEmail()).isEqualTo("user1@example.com");
	}
}
