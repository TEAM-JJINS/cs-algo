package kr.co.csalgo.web.subscription.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.web.subscription.dto.EmailVerificationDto;
import kr.co.csalgo.web.subscription.dto.EmailVerificationVerifyDto;
import kr.co.csalgo.web.subscription.dto.SubscriptionDto;
import kr.co.csalgo.web.subscription.dto.VerificationCodeType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionRestClient {
	private final RestClient restClient;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public ResponseEntity<?> emailVerificationRequest(String email) {
		EmailVerificationDto.Request body = new EmailVerificationDto.Request(email, VerificationCodeType.SUBSCRIPTION);
		return postAndHandle("/auth/email-verifications/request", body, EmailVerificationDto.Response.class);
	}

	public ResponseEntity<?> emailVerificationVerify(String email, String code) {
		EmailVerificationVerifyDto.Request body = new EmailVerificationVerifyDto.Request(email, code, VerificationCodeType.SUBSCRIPTION);
		return postAndHandle("/auth/email-verifications/verify", body, EmailVerificationVerifyDto.Response.class);
	}

	public ResponseEntity<?> subscribe(String email) {
		SubscriptionDto.Request body = new SubscriptionDto.Request(email);
		return postAndHandle("/subscriptions", body, SubscriptionDto.Response.class);
	}

	private <T, R> ResponseEntity<?> postAndHandle(String uri, T requestBody, Class<R> responseType) {
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
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
