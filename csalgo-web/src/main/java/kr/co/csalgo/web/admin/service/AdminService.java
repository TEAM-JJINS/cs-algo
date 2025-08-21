package kr.co.csalgo.web.admin.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.csalgo.web.admin.client.AdminRestClient;
import kr.co.csalgo.web.admin.dto.AdminLoginDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final AdminRestClient adminRestClient;

	public ResponseEntity<?> login(String email, String password) {
		return adminRestClient.login(new AdminLoginDto.Request(email, password));
	}

	public ResponseEntity<?> getUser(String accessToken, String refreshToken, int id) {
		return adminRestClient.getUser(accessToken, refreshToken, id);
	}

	public ResponseEntity<?> getUserList(String accessToken, String refreshToken, int page, int size) {
		return adminRestClient.getUserList(accessToken, refreshToken, page, size);
	}
}

