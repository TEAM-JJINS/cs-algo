package kr.co.csalgo.application.admin.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.application.admin.dto.UpdateRoleDto;
import kr.co.csalgo.common.message.CommonResponse;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.user.service.UserService;
import kr.co.csalgo.domain.user.type.Role;

@ExtendWith(MockitoExtension.class)
class UpdateRoleUseCaseTest {

	@Mock
	private UserService userService;

	private UpdateRoleUseCase updateRoleUseCase;

	@BeforeEach
	void setUp() {
		updateRoleUseCase = new UpdateRoleUseCase(userService);
	}

	@Test
	@DisplayName("유저 권한을 성공적으로 변경하면 CommonResponse를 반환한다.")
	void testUpdateRoleSuccess() {
		Long targetUserId = 1L;
		UpdateRoleDto dto = new UpdateRoleDto(Role.ADMIN);

		CommonResponse response = updateRoleUseCase.updateRole(targetUserId, dto);

		verify(userService).update(targetUserId, Role.ADMIN);
		assertNotNull(response);
		assertEquals(MessageCode.UPDATE_ROLE_SUCCESS.getMessage(), response.getMessage());
	}
}
