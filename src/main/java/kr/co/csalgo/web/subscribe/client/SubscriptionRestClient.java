package kr.co.csalgo.web.subscribe.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import kr.co.csalgo.web.subscribe.dto.EmailVerificationRequest;
import kr.co.csalgo.web.subscribe.dto.EmailVerificationVerifyRequest;
import kr.co.csalgo.web.subscribe.dto.Subscription;
import kr.co.csalgo.web.subscribe.dto.VerificationCodeType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionRestClient {
	private final RestClient restClient;

	public void emailVerificationRequest(String email) {
		restClient.post()
			.uri("/auth/email-verifications/request")
			.body(new EmailVerificationRequest(email, VerificationCodeType.SUBSCRIPTION))
			.retrieve()
			.toBodilessEntity();
	}

	public void emailVerificationVerify(String email, String code) {
		restClient.post()
			.uri("/auth/email-verifications/verify")
			.body(new EmailVerificationVerifyRequest(email, code, VerificationCodeType.SUBSCRIPTION))
			.retrieve()
			.toBodilessEntity();
	}

	public void subscribe(String email) {
		restClient.post()
			.uri("/subscriptions")
			.body(new Subscription(email))
			.retrieve()
			.toBodilessEntity();
	}
}
