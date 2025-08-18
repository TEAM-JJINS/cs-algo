package kr.co.csalgo.server.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.csalgo.application.admin.dto.AdminLoginDto;
import kr.co.csalgo.application.admin.dto.AdminRefreshDto;
import kr.co.csalgo.application.admin.usecase.AdminLoginUseCase;
import kr.co.csalgo.application.admin.usecase.AdminRefreshTokenUseCase;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {
	private final AdminLoginUseCase adminLoginUseCase;
	private final AdminRefreshTokenUseCase adminRefreshTokenUseCase;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AdminLoginDto.Request request) {
		return ResponseEntity.ok(adminLoginUseCase.login(request));
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestBody AdminRefreshDto.Request request) {
		return ResponseEntity.ok(adminRefreshTokenUseCase.refresh(request));
	}
}
