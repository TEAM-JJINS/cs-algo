package kr.co.csalgo.web.subscribe.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.web.subscribe.dto.EmailVerificationDto;
import kr.co.csalgo.web.subscribe.dto.EmailVerificationVerifyDto;
import kr.co.csalgo.web.subscribe.dto.SubscriptionDto;
import kr.co.csalgo.web.subscribe.dto.VerificationCodeType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionRestClient {
	private final RestClient restClient;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public ResponseEntity<?> emailVerificationRequest(String email) {
		EmailVerificationDto.Request body = new EmailVerificationDto.Request(email, VerificationCodeType.SUBSCRIPTION);
		return postAndHandle("/auth/email-verifications/request", body, EmailVerificationDto.Response.class, "인증 요청 처리 중 예외가 발생했습니다.");
	}

	public ResponseEntity<?> emailVerificationVerify(String email, String code) {
		EmailVerificationVerifyDto.Request body = new EmailVerificationVerifyDto.Request(email, code, VerificationCodeType.SUBSCRIPTION);
		return postAndHandle("/auth/email-verifications/verify", body, EmailVerificationVerifyDto.Response.class, "인증 코드 검증 중 예외가 발생했습니다.");
	}

	public ResponseEntity<?> subscribe(String email) {
		SubscriptionDto.Request body = new SubscriptionDto.Request(email);
		return postAndHandle("/subscriptions", body, SubscriptionDto.Response.class, "구독 요청 중 예외가 발생했습니다.");
	}

	private <T, R> ResponseEntity<?> postAndHandle(String uri, T requestBody, Class<R> responseType, String fallbackErrorMessage) {
		try {
			return restClient.post()
				.uri(uri)
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
			return ResponseEntity.internalServerError().body(fallbackErrorMessage);
		}
	}
}
