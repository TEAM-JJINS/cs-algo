package kr.co.csalgo.web.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.csalgo.web.admin.dto.UserDto;
import kr.co.csalgo.web.admin.service.AdminService;
import kr.co.csalgo.web.common.dto.PagedResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminWebController {
	private final AdminService adminService;

	@GetMapping("/login")
	public String mainPage() {
		return "admin/login";
	}

	@GetMapping("/dashboard")
	public String dashboard(
		@CookieValue("accessToken") String accessToken,
		Model model
	) {
		// 회원 수 조회 (0페이지, size=1로 최소 조회)
		ResponseEntity<?> userResponse = adminService.getUserList(accessToken, 1, 1);
		@SuppressWarnings("unchecked")
		PagedResponse<UserDto.Response> userBody = (PagedResponse<UserDto.Response>)userResponse.getBody();
		long userCount = (userBody != null) ? userBody.getTotalElements() : 0;

		// TODO: 문제 수 조회 (마찬가지로 최소 페이지 조회)

		model.addAttribute("userCount", userCount);
		model.addAttribute("questionCount", "...");
		model.addAttribute("activeMenu", "dashboard");
		return "admin/dashboard/index";
	}

	@GetMapping("/dashboard/users")
	public String users(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@CookieValue("accessToken") String accessToken,
		Model model
	) {
		ResponseEntity<?> response = adminService.getUserList(accessToken, page, size);
		@SuppressWarnings("unchecked")
		PagedResponse<UserDto.Response> body = (PagedResponse<UserDto.Response>)response.getBody();

		model.addAttribute("users", body.getContent());
		model.addAttribute("currentPage", body.getCurrentPage());
		model.addAttribute("totalPages", body.getTotalPages());
		model.addAttribute("first", body.isFirst());
		model.addAttribute("last", body.isLast());
		model.addAttribute("activeMenu", "users");

		return "admin/dashboard/users";
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
		return adminService.login(email, password); // JSON 반환
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestParam String refreshToken) {
		return adminService.refresh(refreshToken);
	}
}
