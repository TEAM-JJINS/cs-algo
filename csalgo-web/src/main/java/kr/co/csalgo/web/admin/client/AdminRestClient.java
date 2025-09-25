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

import com.fasterxml.jackson.databind.ObjectMapper;

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
	private final ObjectMapper objectMapper = new ObjectMapper();

	/** 로그인 */
	public ResponseEntity<?> login(AdminLoginDto.Request body) {
		return postAndHandle("/admin/login", body, AdminLoginDto.Response.class);
	}

	/** 토큰 재발급 */
	public ResponseEntity<?> refresh(AdminRefreshDto.Request body) {
		return postAndHandle("/admin/refresh", body, AdminRefreshDto.Response.class);
	}

	/** 로그아웃 */
	public ResponseEntity<?> logout(AdminRefreshDto.Request body, HttpServletResponse response) {
		ResponseEntity<?> result = postAndHandle("/admin/logout", body, MessageResponseDto.class);
		expireCookie(response, "refreshToken");
		return result;
	}

	/** 사용자 목록 조회 */
	public ResponseEntity<?> getUserList(String accessToken, String refreshToken, int page, int size, HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> getAndHandle("/users?page={page}&size={size}",
				new ParameterizedTypeReference<PagedResponse<UserDto.Response>>() {
				},
				token, page, size),
			response
		);
	}

	/** 사용자 상세 조회 */
	public ResponseEntity<?> getUserDetail(String accessToken, String refreshToken, Long userId, HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> getAndHandle("/users/{userId}", UserDto.Response.class, token, userId),
			response
		);
	}

	/** 사용자 권한 수정 */
	public ResponseEntity<?> updateUserRole(String accessToken, String refreshToken, Long userId, UserDto.Request body,
		HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> putAndHandle("/users/{userId}/role", body, UserDto.Response.class, token, userId),
			response
		);
	}

	/** 사용자 삭제 */
	public ResponseEntity<?> deleteUser(String accessToken, String refreshToken, Long userId, HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> deleteAndHandle("/users/{userId}", MessageResponseDto.class, token, userId),
			response
		);
	}

	/** 문제 목록 조회 */
	public ResponseEntity<?> getQuestionList(String accessToken, String refreshToken, int page, int size, HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> getAndHandle("/questions?page={page}&size={size}",
				new ParameterizedTypeReference<PagedResponse<QuestonDto.Response>>() {
				},
				token, page, size),
			response
		);
	}

	/** 문제 상세 조회 */
	public ResponseEntity<?> getQuestionDetail(String accessToken, String refreshToken, Long questionId, HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> getAndHandle("/questions/{id}", QuestonDto.Response.class, token, questionId),
			response
		);
	}

	/** 문제 수정 */
	public ResponseEntity<?> updateQuestion(String accessToken, String refreshToken, Long questionId, QuestonDto.Request body,
		HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> putAndHandle("/questions/{id}", body, MessageResponseDto.class, token, questionId),
			response
		);
	}

	/** 문제 삭제 */
	public ResponseEntity<?> deleteQuestion(String accessToken, String refreshToken, Long questionId, HttpServletResponse response) {
		return executeWithRetry(
			accessToken,
			refreshToken,
			token -> deleteAndHandle("/questions/{id}", MessageResponseDto.class, token, questionId),
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
			if ((ex.getRawStatusCode() == 401) && refreshToken != null) {
				ResponseEntity<?> refreshRes = this.refresh(new AdminRefreshDto.Request(refreshToken));

				if (refreshRes.getStatusCode().is2xxSuccessful() && refreshRes.getBody() instanceof AdminRefreshDto.Response body) {
					String newAccessToken = body.getAccessToken();
					String newRefreshToken = body.getRefreshToken();

					updateRefreshTokenCookie(response, newRefreshToken);
					return requestFn.apply(newAccessToken);
				}
			}
			throw ex;
		}
	}

	/** 공통 POST 처리 */
	private <T, R> ResponseEntity<?> postAndHandle(String uri, T requestBody, Class<R> responseType, Object... uriVars) {
		try {
			return restClient.post()
				.uri(uri, uriVars)
				.body(requestBody)
				.exchange((req, res) -> {
					String body = res.bodyTo(String.class);
					if (res.getStatusCode().is2xxSuccessful()) {
						R dto = objectMapper.readValue(body, responseType);
						return ResponseEntity.ok(dto);
					} else {
						return ResponseEntity.status(res.getStatusCode()).body(body);
					}
				});
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	/** 공통 GET 처리 */
	private <R> ResponseEntity<?> getAndHandle(String uri, Class<R> responseType, String token, Object... uriVars) {
		try {
			return restClient.get()
				.uri(uri, uriVars)
				.header("Authorization", "Bearer " + token)
				.exchange((req, res) -> {
					String body = res.bodyTo(String.class);
					if (res.getStatusCode().is2xxSuccessful()) {
						R dto = objectMapper.readValue(body, responseType);
						return ResponseEntity.ok(dto);
					} else {
						return ResponseEntity.status(res.getStatusCode()).body(body);
					}
				});
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	/** 공통 GET 처리 (제네릭 응답) */
	private <R> ResponseEntity<?> getAndHandle(String uri, ParameterizedTypeReference<R> responseType, String token, Object... uriVars) {
		try {
			return restClient.get()
				.uri(uri, uriVars)
				.header("Authorization", "Bearer " + token)
				.exchange((req, res) -> {
					String body = res.bodyTo(String.class);
					if (res.getStatusCode().is2xxSuccessful()) {
						R dto = objectMapper.readValue(body,
							objectMapper.getTypeFactory().constructType(responseType.getType()));
						return ResponseEntity.ok(dto);
					} else {
						return ResponseEntity.status(res.getStatusCode()).body(body);
					}
				});
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	/** 공통 PUT 처리 */
	private <T, R> ResponseEntity<?> putAndHandle(String uri, T requestBody, Class<R> responseType, String token, Object... uriVars) {
		try {
			return restClient.put()
				.uri(uri, uriVars)
				.header("Authorization", "Bearer " + token)
				.body(requestBody)
				.exchange((req, res) -> {
					String body = res.bodyTo(String.class);
					if (res.getStatusCode().is2xxSuccessful()) {
						R dto = objectMapper.readValue(body, responseType);
						return ResponseEntity.ok(dto);
					} else {
						return ResponseEntity.status(res.getStatusCode()).body(body);
					}
				});
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	/** 공통 DELETE 처리 */
	private <R> ResponseEntity<?> deleteAndHandle(String uri, Class<R> responseType, String token, Object... uriVars) {
		try {
			return restClient.delete()
				.uri(uri, uriVars)
				.header("Authorization", "Bearer " + token)
				.exchange((req, res) -> {
					String body = res.bodyTo(String.class);
					if (res.getStatusCode().is2xxSuccessful()) {
						if (responseType == Void.class) {
							return ResponseEntity.ok().build();
						}
						R dto = objectMapper.readValue(body, responseType);
						return ResponseEntity.ok(dto);
					} else {
						return ResponseEntity.status(res.getStatusCode()).body(body);
					}
				});
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	/** Refresh Token 쿠키 업데이트 */
	private void updateRefreshTokenCookie(HttpServletResponse response, String newRefreshToken) {
		ResponseCookie cookie = ResponseCookie.from("refreshToken", "test")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.path("/")
			.maxAge(Duration.ofDays(30))
			.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	private void expireCookie(HttpServletResponse response, String name) {
		ResponseCookie cookie = ResponseCookie.from(name, "")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.path("/")
			.maxAge(0)
			.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
