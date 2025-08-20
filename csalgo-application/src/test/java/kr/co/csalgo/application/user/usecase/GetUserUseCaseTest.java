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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import kr.co.csalgo.application.common.dto.PagedResponse;
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
		// given
		List<User> users = List.of(
			User.builder().email("user1@example.com").build(),
			User.builder().email("user2@example.com").build()
		);
		Pageable pageable = PageRequest.of(0, 2);
		Page<User> page = new PageImpl<>(users, pageable, users.size());

		when(userService.list(pageable)).thenReturn(page);

		// when
		PagedResponse<UserDto.Response> result = getUserUseCase.getUserListWithPaging(1, 2);

		// then
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent().get(0).getEmail()).isEqualTo("user1@example.com");
		assertThat(result.getContent().get(1).getEmail()).isEqualTo("user2@example.com");
		assertThat(result.getCurrentPage()).isEqualTo(1);
		assertThat(result.getTotalPages()).isEqualTo(1);
		assertThat(result.isFirst()).isTrue();
		assertThat(result.isLast()).isTrue();
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
