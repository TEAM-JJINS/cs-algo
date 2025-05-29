package kr.co.csalgo.web.subscribe.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import kr.co.csalgo.web.subscribe.dto.EmailVerificationDto;
import kr.co.csalgo.web.subscribe.dto.EmailVerificationVerifyDto;
import kr.co.csalgo.web.subscribe.dto.SubscriptionDto;
import kr.co.csalgo.web.subscribe.dto.VerificationCodeType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionRestClient {
	private final RestClient restClient;

	public ResponseEntity<EmailVerificationDto.Response> emailVerificationRequest(String email) {
		return restClient.post()
			.uri("/auth/email-verifications/request")
			.body(new EmailVerificationDto.Request(email, VerificationCodeType.SUBSCRIPTION))
			.retrieve()
			.toEntity(EmailVerificationDto.Response.class);
	}

	public ResponseEntity<EmailVerificationVerifyDto.Response> emailVerificationVerify(String email, String code) {
		return restClient.post()
			.uri("/auth/email-verifications/verify")
			.body(new EmailVerificationVerifyDto.Request(email, code, VerificationCodeType.SUBSCRIPTION))
			.retrieve()
			.toEntity(EmailVerificationVerifyDto.Response.class);
	}

	public ResponseEntity<SubscriptionDto.Response> subscribe(String email) {
		return restClient.post()
			.uri("/subscriptions")
			.body(new SubscriptionDto.Request(email))
			.retrieve()
			.toEntity(SubscriptionDto.Response.class);
	}
}
