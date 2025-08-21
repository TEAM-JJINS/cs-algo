package kr.co.csalgo.web.admin.client;

import java.util.function.Function;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import kr.co.csalgo.web.admin.dto.AdminLoginDto;
import kr.co.csalgo.web.admin.dto.AdminRefreshDto;
import kr.co.csalgo.web.admin.dto.UserDto;
import kr.co.csalgo.web.common.dto.PagedResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminRestClient {
	private final RestClient restClient;

	/** 로그인 */
	public ResponseEntity<AdminLoginDto.Response> login(AdminLoginDto.Request body) {
		return restClient.post()
			.uri("/admin/login")
			.body(body)
			.retrieve()
			.toEntity(AdminLoginDto.Response.class);
	}

	/** 토큰 재발급 */
	public ResponseEntity<AdminRefreshDto.Response> refresh(AdminRefreshDto.Request body) {
		return restClient.post()
			.uri("/admin/refresh")
			.body(body)
			.retrieve()
			.toEntity(AdminRefreshDto.Response.class);
	}

	/** 단일 사용자 조회 */
	public ResponseEntity<UserDto.Response> getUser(String accessToken, String refreshToken, int id) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.get()
				.uri("/users/{id}", id)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.toEntity(UserDto.Response.class)
		);
	}

	/** 사용자 목록 조회 */
	public ResponseEntity<PagedResponse<UserDto.Response>> getUserList(String accessToken, String refreshToken, int page, int size) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.get()
				.uri("/users?page={page}&size={size}", page, size)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.toEntity(new ParameterizedTypeReference<PagedResponse<UserDto.Response>>() {
				})
		);
	}

	/** 공통 재시도 로직 */
	private <T> ResponseEntity<T> executeWithRetry(
		String accessToken,
		String refreshToken,
		Function<String, ResponseEntity<T>> requestFn
	) {
		try {
			return requestFn.apply(accessToken);
		} catch (RestClientResponseException ex) {
			// 401일 경우 Refresh 시도
			if ((ex.getRawStatusCode() == 401) && refreshToken != null) {
				ResponseEntity<AdminRefreshDto.Response> refreshRes =
					this.refresh(new AdminRefreshDto.Request(refreshToken));

				if (refreshRes.getStatusCode().is2xxSuccessful() && refreshRes.getBody() != null) {
					String newAccessToken = refreshRes.getBody().getAccessToken();
					return requestFn.apply(newAccessToken);
				}
			}
			// 다른 오류는 그대로 throw
			throw ex;
		}
	}

}
