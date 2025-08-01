package kr.co.csalgo.application.user.usecase;

import org.springframework.stereotype.Service;

import kr.co.csalgo.common.message.CommonResponse;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteUserUseCase {
	private final UserService userService;

	public CommonResponse deleteUser(Long userId) {
		userService.delete(userId);
		log.info("[회원 삭제] userId:{} 삭제 완료", userId);
		return new CommonResponse(MessageCode.DELETE_USER_SUCCESS.getMessage());
	}
}
