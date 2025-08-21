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
	public String dashboard(Model model) {
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
