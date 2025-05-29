package kr.co.csalgo.web.subscription.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.csalgo.web.subscription.client.SubscriptionRestClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
	private final SubscriptionRestClient subscriptionRestClient;

	public ResponseEntity<?> emailVerificationRequest(String email) {
		ResponseEntity<?> response = subscriptionRestClient.emailVerificationRequest(email);
		if (!response.getStatusCode().is2xxSuccessful()) {
			return response;
		}
		return ResponseEntity.ok(response.getBody());
	}

	public ResponseEntity<?> emailVerificationVerify(String email, String code) {
		ResponseEntity<?> response = subscriptionRestClient.emailVerificationVerify(email, code);
		if (!response.getStatusCode().is2xxSuccessful()) {
			return response;
		}
		return ResponseEntity.ok(response.getBody());
	}

	public ResponseEntity<?> subscribe(String email) {
		ResponseEntity<?> response = subscriptionRestClient.subscribe(email);
		if (!response.getStatusCode().is2xxSuccessful()) {
			return response;
		}
		return ResponseEntity.ok(response.getBody());
	}
}
