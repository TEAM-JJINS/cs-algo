package kr.co.csalgo.server.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import kr.co.csalgo.application.admin.dto.UpdateRoleDto;
import kr.co.csalgo.application.admin.usecase.UpdateRoleUseCase;
import kr.co.csalgo.application.user.usecase.DeleteUserUseCase;
import kr.co.csalgo.application.user.usecase.GetUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {
	private final GetUserUseCase getUserUseCase;
	private final DeleteUserUseCase deleteUserUseCase;
	private final UpdateRoleUseCase updateRoleUseCase;

	@GetMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Operation(summary = "사용자 목록 조회", description = "관리자는 사용자 목록을 조회할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공")
	public ResponseEntity<?> getUserList(
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(getUserUseCase.getUserListWithPaging(page, size));
	}

	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Operation(summary = "사용자 상세 조회", description = "관리자는 사용자 정보를 조회할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "사용자 상세 조회 성공")
	public ResponseEntity<?> getUserDetail(@PathVariable Long userId) {
		return ResponseEntity.ok(getUserUseCase.getUserDetail(userId));
	}

	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Operation(summary = "사용자 삭제", description = "관리자는 사용자 정보를 삭제 할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "사용자 삭제 성공")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		return ResponseEntity.ok(deleteUserUseCase.deleteUser(userId));
	}

	@PutMapping("/{id}/role")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Operation(summary = "사용자 권한 수정", description = "관리자는 사용자의 권한을 변경할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "사용자 권한 수정 성공")
	public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody UpdateRoleDto request) {
		return ResponseEntity.ok(updateRoleUseCase.updateRole(id, request));
	}
}
