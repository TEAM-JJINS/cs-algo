package kr.co.csalgo.web.admin.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.csalgo.web.admin.client.AdminRestClient;
import kr.co.csalgo.web.admin.dto.AdminLoginDto;
import kr.co.csalgo.web.admin.dto.QuestonDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final AdminRestClient adminRestClient;

	public ResponseEntity<?> login(String email, String password) {
		return adminRestClient.login(new AdminLoginDto.Request(email, password));
	}

	public ResponseEntity<?> getUserList(String accessToken, String refreshToken, int page, int size, HttpServletResponse httpServletResponse) {
		return adminRestClient.getUserList(accessToken, refreshToken, page, size, httpServletResponse);
	}

	public ResponseEntity<?> getUser(String accessToken, String refreshToken, Long userId, HttpServletResponse httpServletResponse) {
		return adminRestClient.getUserDetail(accessToken, refreshToken, userId, httpServletResponse);
	}

	public ResponseEntity<?> updateUserRole(String accessToken, String refreshToken, Long userId, String role,
		HttpServletResponse httpServletResponse) {
		return adminRestClient.updateUserRole(accessToken, refreshToken, userId, role, httpServletResponse);
	}

	public ResponseEntity<?> deleteUser(String accessToken, String refreshToken, Long userId, HttpServletResponse httpServletResponse) {
		return adminRestClient.deleteUser(accessToken, refreshToken, userId, httpServletResponse);
	}

	public ResponseEntity<?> getQuestionList(String accessToken, String refreshToken, int page, int size, HttpServletResponse httpServletResponse) {
		return adminRestClient.getQuestionList(accessToken, refreshToken, page, size, httpServletResponse);
	}

	public ResponseEntity<?> getQuestion(String accessToken, String refreshToken, Long questionId, HttpServletResponse httpServletResponse) {
		return adminRestClient.getQuestionDetail(accessToken, refreshToken, questionId, httpServletResponse);
	}

	public ResponseEntity<?> updateQuestion(String accessToken, String refreshToken, Long questionId, QuestonDto.Request request,
		HttpServletResponse httpServletResponse) {
		return adminRestClient.updateQuestion(accessToken, refreshToken, questionId, request, httpServletResponse);
	}

	public ResponseEntity<?> deleteQuestion(String accessToken, String refreshToken, Long questionId, HttpServletResponse httpServletResponse) {
		return adminRestClient.deleteQuestion(accessToken, refreshToken, questionId, httpServletResponse);
	}
}

