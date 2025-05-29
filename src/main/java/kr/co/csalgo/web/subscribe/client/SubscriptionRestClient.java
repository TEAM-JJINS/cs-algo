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
		try {
			return restClient.post()
				.uri("/auth/email-verifications/request")
				.body(new EmailVerificationDto.Request(email, VerificationCodeType.SUBSCRIPTION))
				.exchange((request, response) -> {
					if (response.getStatusCode().is2xxSuccessful()) {
						EmailVerificationDto.Response dto = objectMapper.readValue(
							response.bodyTo(String.class),
							EmailVerificationDto.Response.class);
						return ResponseEntity.ok(dto);
					} else {
						return ResponseEntity.status(response.getStatusCode()).body(response.bodyTo(String.class));
					}
				});
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("인증 요청 처리 중 예외가 발생했습니다.");
		}
	}

	public ResponseEntity<?> emailVerificationVerify(String email, String code) {
		try {
			return restClient.post()
				.uri("/auth/email-verifications/verify")
				.body(new EmailVerificationVerifyDto.Request(email, code, VerificationCodeType.SUBSCRIPTION))
				.exchange((request, response) -> {
					String body = response.bodyTo(String.class);
					if (response.getStatusCode().is2xxSuccessful()) {
						EmailVerificationVerifyDto.Response dto = objectMapper.readValue(body, EmailVerificationVerifyDto.Response.class);
						return ResponseEntity.ok(dto);
					} else {
						return ResponseEntity.status(response.getStatusCode()).body(body);
					}
				});
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("인증 코드 검증 중 예외가 발생했습니다.");
		}
	}

	public ResponseEntity<?> subscribe(String email) {
		try {
			return restClient.post()
				.uri("/subscriptions")
				.body(new SubscriptionDto.Request(email))
				.exchange((request, response) -> {
					String body = response.bodyTo(String.class);
					if (response.getStatusCode().is2xxSuccessful()) {
						SubscriptionDto.Response dto = objectMapper.readValue(body, SubscriptionDto.Response.class);
						return ResponseEntity.ok(dto);
					} else {
						return ResponseEntity.status(response.getStatusCode()).body(body);
					}
				});
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("구독 요청 중 예외가 발생했습니다.");
		}
	}
}
