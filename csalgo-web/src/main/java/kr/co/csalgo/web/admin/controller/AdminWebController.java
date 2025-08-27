package kr.co.csalgo.web.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.csalgo.web.admin.dto.QuestonDto;
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
		@CookieValue("refreshToken") String refreshToken,
		HttpServletResponse httpServletResponse,
		Model model
	) {
		// 회원 수 조회 (0페이지, size=1로 최소 조회)
		ResponseEntity<?> userResponse = adminService.getUserList(accessToken, refreshToken, 1, 1, httpServletResponse);
		@SuppressWarnings("unchecked")
		PagedResponse<UserDto.Response> userBody = (PagedResponse<UserDto.Response>)userResponse.getBody();
		long userCount = (userBody != null) ? userBody.getTotalElements() : 0;

		ResponseEntity<?> questionResponse = adminService.getQuestionList(accessToken, refreshToken, 1, 1, httpServletResponse);
		@SuppressWarnings("unchecked")
		PagedResponse<QuestonDto.Response> questionBody = (PagedResponse<QuestonDto.Response>)questionResponse.getBody();
		long questionCount = (questionBody != null) ? questionBody.getTotalElements() : 0;

		model.addAttribute("userCount", userCount);
		model.addAttribute("questionCount", questionCount);
		model.addAttribute("activeMenu", "dashboard");
		return "admin/dashboard/index";
	}

	@GetMapping("/dashboard/users")
	public String users(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@CookieValue("accessToken") String accessToken,
		@CookieValue("refreshToken") String refreshToken,
		HttpServletResponse httpServletResponse,
		Model model
	) {
		ResponseEntity<?> response = adminService.getUserList(accessToken, refreshToken, page, size, httpServletResponse);
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

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<?> deleteUser(
		@CookieValue("accessToken") String accessToken,
		@CookieValue("refreshToken") String refreshToken,
		@PathVariable Long userId,
		HttpServletResponse httpServletResponse
	) {
		return adminService.deleteUser(accessToken, refreshToken, userId, httpServletResponse);
	}

	@GetMapping("/dashboard/questions")
	public String questions(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@CookieValue("accessToken") String accessToken,
		@CookieValue("refreshToken") String refreshToken,
		HttpServletResponse httpServletResponse,
		Model model
	) {
		ResponseEntity<?> response = adminService.getQuestionList(accessToken, refreshToken, page, size, httpServletResponse);
		@SuppressWarnings("unchecked")
		PagedResponse<QuestonDto.Response> body = (PagedResponse<QuestonDto.Response>)response.getBody();

		model.addAttribute("questions", body.getContent());
		model.addAttribute("currentPage", body.getCurrentPage());
		model.addAttribute("totalPages", body.getTotalPages());
		model.addAttribute("first", body.isFirst());
		model.addAttribute("last", body.isLast());
		model.addAttribute("activeMenu", "questions");

		return "admin/dashboard/questions";
	}

	@GetMapping("/dashboard/questions/{questionId}")
	public String questionDetail(
		@CookieValue("accessToken") String accessToken,
		@CookieValue("refreshToken") String refreshToken,
		@PathVariable Long questionId,
		HttpServletResponse httpServletResponse,
		Model model
	) {
		ResponseEntity<?> response = adminService.getQuestion(accessToken, refreshToken, questionId, httpServletResponse);
		QuestonDto.Response body = (QuestonDto.Response)response.getBody();

		model.addAttribute("question", body);
		model.addAttribute("activeMenu", "questions");

		return "admin/dashboard/question";
	}

	@DeleteMapping("/questions/{questionId}")
	public ResponseEntity<?> deleteQuestion(
		@CookieValue("accessToken") String accessToken,
		@CookieValue("refreshToken") String refreshToken,
		@PathVariable Long questionId,
		HttpServletResponse httpServletResponse
	) {
		return adminService.deleteQuestion(accessToken, refreshToken, questionId, httpServletResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
		return adminService.login(email, password);
	}
}
