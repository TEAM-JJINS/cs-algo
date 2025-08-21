package kr.co.csalgo.web.admin.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import kr.co.csalgo.web.admin.dto.AdminLoginDto;
import kr.co.csalgo.web.admin.dto.AdminRefreshDto;
import kr.co.csalgo.web.admin.dto.UserDto;
import kr.co.csalgo.web.common.dto.PagedResponse;
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

	public ResponseEntity<?> getUser(String accessToken, int id) {
		return restClient.get()
			.uri("/users/{id}", id)
			.header("Authorization", "Bearer " + accessToken)
			.retrieve()
			.toEntity(UserDto.Response.class);
	}

	public ResponseEntity<?> getUserList(String accessToken, int page, int size) {
		return restClient.get()
			.uri("/users?page={page}&size={size}", page, size)
			.header("Authorization", "Bearer " + accessToken)
			.retrieve()
			.toEntity(new ParameterizedTypeReference<PagedResponse<UserDto.Response>>() {
			});
	}
}
