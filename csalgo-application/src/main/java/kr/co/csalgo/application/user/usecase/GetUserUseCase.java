package kr.co.csalgo.application.user.usecase;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.co.csalgo.application.common.dto.PagedResponse;
import kr.co.csalgo.application.user.dto.UserDto;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetUserUseCase {
	private final UserService userService;

	public PagedResponse<UserDto.Response> getUserListWithPaging(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<User> resultPage = userService.list(pageable);

		List<UserDto.Response> users = resultPage.getContent().stream()
			.map(UserDto.Response::of)
			.toList();

		return PagedResponse.<UserDto.Response>builder()
			.content(users)
			.currentPage(resultPage.getNumber() + 1) // 0 기반 → 1 기반 변환
			.totalPages(resultPage.getTotalPages())
			.totalElements(resultPage.getTotalElements())
			.first(resultPage.isFirst())
			.last(resultPage.isLast())
			.build();
	}

	public UserDto.Response getUserDetail(Long userId) {
		return UserDto.Response.of(userService.read(userId));
	}
}
