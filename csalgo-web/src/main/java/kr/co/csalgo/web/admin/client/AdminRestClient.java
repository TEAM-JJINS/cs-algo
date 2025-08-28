package kr.co.csalgo.web.admin.client;

import java.time.Duration;
import java.util.function.Function;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.csalgo.web.admin.dto.AdminLoginDto;
import kr.co.csalgo.web.admin.dto.AdminRefreshDto;
import kr.co.csalgo.web.admin.dto.QuestonDto;
import kr.co.csalgo.web.admin.dto.UserDto;
import kr.co.csalgo.web.common.dto.MessageResponseDto;
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

	/** 사용자 목록 조회 */
	public ResponseEntity<PagedResponse<UserDto.Response>> getUserList(String accessToken, String refreshToken, int page, int size,
		HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.get()
				.uri("/users?page={page}&size={size}", page, size)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.toEntity(new ParameterizedTypeReference<PagedResponse<UserDto.Response>>() {
				}),
			response
		);
	}

	/** 사용자 상세 조회 */
	public ResponseEntity<UserDto.Response> getUserDetail(String accessToken, String refreshToken, Long userId, HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.get()
				.uri("/users/{userId}", userId)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.toEntity(UserDto.Response.class),
			response
		);
	}

	/** 사용자 권한 수정 */
	public ResponseEntity<UserDto.Response> updateUserRole(String accessToken, String refreshToken, Long userId, String role,
		HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.put()
				.uri("/users/{userId}/role?role={role}", userId, role)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.toEntity(UserDto.Response.class),
			response
		);
	}

	/** 사용자 삭제 */
	public ResponseEntity<Void> deleteUser(String accessToken, String refreshToken, Long userId, HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.delete()
				.uri("/users/{userId}", userId)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.toEntity(Void.class),
			response
		);
	}

	/** 문제 목록 조회 */
	public ResponseEntity<PagedResponse<QuestonDto.Response>> getQuestionList(String accessToken, String refreshToken, int page, int size,
		HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.get()
				.uri("/questions?page={page}&size={size}", page, size)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.toEntity(new ParameterizedTypeReference<PagedResponse<QuestonDto.Response>>() {
				}),
			response
		);
	}

	/** 문제 상세 조회 */
	public ResponseEntity<QuestonDto.Response> getQuestionDetail(String accessToken, String refreshToken, Long questionId,
		HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.get()
				.uri("/questions/{id}", questionId)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.toEntity(QuestonDto.Response.class),
			response
		);
	}

	/** 문제 수정 */
	public ResponseEntity<MessageResponseDto> updateQuestion(String accessToken, String refreshToken, Long questionId, QuestonDto.Request body,
		HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.put()
				.uri("/questions/{id}", questionId)
				.header("Authorization", "Bearer " + token)
				.body(body)
				.retrieve()
				.toEntity(MessageResponseDto.class),
			response
		);
	}

	/** 문제 삭제 */
	public ResponseEntity<MessageResponseDto> deleteQuestion(String accessToken, String refreshToken, Long questionId, HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> restClient.delete()
				.uri("/questions/{id}", questionId)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.toEntity(MessageResponseDto.class),
			response
		);
	}

	/** 공통 재시도 로직 */
	private <T> ResponseEntity<T> executeWithRetry(
		String accessToken,
		String refreshToken,
		Function<String, ResponseEntity<T>> requestFn,
		HttpServletResponse response
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
					String newRefreshToken = refreshRes.getBody().getRefreshToken();

					// 2. 새로운 RefreshToken을 쿠키에 저장
					updateRefreshTokenCookie(response, newRefreshToken);

					// 3. 새로운 AccessToken으로 재호출
					return requestFn.apply(newAccessToken);
				}
			}
			// 다른 오류는 그대로 throw
			throw ex;
		}
	}

	/** Refresh Token 쿠키 업데이트 */
	private void updateRefreshTokenCookie(HttpServletResponse response, String newRefreshToken) {
		ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(Duration.ofDays(30))
			.secure(true)
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
