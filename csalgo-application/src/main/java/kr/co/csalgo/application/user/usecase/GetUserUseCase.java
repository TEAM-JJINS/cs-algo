package kr.co.csalgo.application.user.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.csalgo.application.user.dto.UserDto;
import kr.co.csalgo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetUserUseCase {
	private final UserService userService;

	public List<UserDto.Response> getUserListWithPaging(int page, int size) {
		List<UserDto.Response> users = userService.list(page - 1, size).stream()
			.map(UserDto.Response::of)
			.toList();
		log.info("[사용자 리스트 조회 완료] count:{}", users.size());
		return users;
	}
}
