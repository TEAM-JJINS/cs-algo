package kr.co.csalgo.web.admin.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import kr.co.csalgo.web.admin.dto.AdminLoginDto;
import kr.co.csalgo.web.admin.dto.AdminRefreshDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminRestClient {
	private final RestClient restClient;

	public ResponseEntity<?> login(AdminLoginDto.Request body) {
		return restClient.post()
			.uri("/admin/login")
			.body(body)
			.retrieve()
			.toEntity(AdminLoginDto.Response.class);
	}

	public ResponseEntity<?> refresh(AdminRefreshDto.Request body) {
		return restClient.post()
			.uri("/admin/refresh")
			.body(body)
			.retrieve()
			.toEntity(AdminRefreshDto.Response.class);
	}
}
