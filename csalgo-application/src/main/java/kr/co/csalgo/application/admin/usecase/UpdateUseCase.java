package kr.co.csalgo.application.admin.usecase;

import org.springframework.stereotype.Service;

import kr.co.csalgo.application.admin.dto.UpdateRoleDto;
import kr.co.csalgo.common.message.CommonResponse;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.user.service.UserService;
import kr.co.csalgo.domain.user.type.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUseCase {
	private final UserService userService;

	public CommonResponse updateRole(Long targetUserId, UpdateRoleDto updateRoleDto) {
		Role newRole = updateRoleDto.getRole();
		userService.update(targetUserId, newRole);
		log.info("[권한 수정] targetUserId={}, newRole={} 로 변경 완료", targetUserId, newRole);
		return new CommonResponse(MessageCode.UPDATE_ROLE_SUCCESS.getMessage());
	}
}
