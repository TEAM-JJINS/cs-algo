package kr.co.csalgo.web.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@GetMapping("/dashboard/members")
	public String members(@PageableDefault(size = 10) Pageable pageable, Model model) {
		// Page<Member> members = memberService.findAll(pageable);
		Page<?> members = Page.empty(pageable);
		model.addAttribute("members", members);
		model.addAttribute("activeMenu", "members");
		return "admin/dashboard/members";
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
