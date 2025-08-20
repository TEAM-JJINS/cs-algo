package kr.co.csalgo.web.admin.controller;

import java.util.List;

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
		List<UserDto.Response> users = (List<UserDto.Response>)response.getBody();
		model.addAttribute("users", users);
		model.addAttribute("currentPage", page);
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
