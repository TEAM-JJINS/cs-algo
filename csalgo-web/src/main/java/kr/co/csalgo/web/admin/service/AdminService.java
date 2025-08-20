package kr.co.csalgo.web.admin.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.csalgo.web.admin.client.AdminRestClient;
import kr.co.csalgo.web.admin.dto.AdminLoginDto;
import kr.co.csalgo.web.admin.dto.AdminRefreshDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final AdminRestClient adminRestClient;

	public ResponseEntity<?> login(String email, String password) {
		return adminRestClient.login(new AdminLoginDto.Request(email, password));
	}

	public ResponseEntity<?> refresh(String refreshToken) {
		return adminRestClient.refresh(new AdminRefreshDto.Request(refreshToken));
	}

	public ResponseEntity<?> getUser(String accessToken, int id) {
		return adminRestClient.getUser(accessToken, id);
	}

	public ResponseEntity<?> getUserList(String accessToken, int page, int size) {
		return adminRestClient.getUserList(accessToken, page, size);
	}
}

