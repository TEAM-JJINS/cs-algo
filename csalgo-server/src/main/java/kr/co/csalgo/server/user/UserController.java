package kr.co.csalgo.server.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import kr.co.csalgo.application.user.usecase.GetUserUseCase;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	private final GetUserUseCase getUserUseCase;

	@GetMapping("")
	@Operation(summary = "사용자 목록 조회", description = "관리자는 사용자 목록을 조회할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공")
	public ResponseEntity<?> getUserList(
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(getUserUseCase.getUserListWithPaging(page, size));
	}

	@GetMapping("/{userId}")
	@Operation(summary = "사용자 상세 조회", description = "관리자는 사용자 정보를 조회할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "사용자 상세 조회 성공")
	public ResponseEntity<?> getUserDetail(@PathVariable Long userId) {
		return ResponseEntity.ok(getUserUseCase.getUserDetail(userId));
	}
}
