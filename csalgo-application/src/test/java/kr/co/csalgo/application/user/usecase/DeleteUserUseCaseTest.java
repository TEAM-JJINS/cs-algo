package kr.co.csalgo.application.user.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.common.message.CommonResponse;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.user.service.UserService;

@DisplayName("DeleteUserUseCase 테스트")
@ExtendWith(MockitoExtension.class)
public class DeleteUserUseCaseTest {
	@Mock
	private UserService userService;

	@InjectMocks
	private DeleteUserUseCase deleteUserUseCase;

	@Test
	@DisplayName("회원 삭제 성공 시 성공 메시지를 반환한다")
	void testDeleteUserSuccess() {
		Long userId = 1L;

		CommonResponse response = deleteUserUseCase.deleteUser(userId);

		assertEquals(MessageCode.DELETE_USER_SUCCESS.getMessage(), response.getMessage());
		verify(userService).delete(userId);
	}
}
