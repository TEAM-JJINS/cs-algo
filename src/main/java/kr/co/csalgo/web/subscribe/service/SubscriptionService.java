package kr.co.csalgo.web.subscribe.service;

import org.springframework.stereotype.Service;

import kr.co.csalgo.web.subscribe.client.SubscriptionRestClient;
import kr.co.csalgo.web.subscribe.dto.EmailVerificationDto;
import kr.co.csalgo.web.subscribe.dto.EmailVerificationVerifyDto;
import kr.co.csalgo.web.subscribe.dto.SubscriptionDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
	private final SubscriptionRestClient subscriptionRestClient;

	public EmailVerificationDto.Response emailVerificationRequest(String email) {
		return subscriptionRestClient.emailVerificationRequest(email).getBody();
	}

	public EmailVerificationVerifyDto.Response emailVerificationVerify(String email, String code) {
		return subscriptionRestClient.emailVerificationVerify(email, code).getBody();
	}

	public SubscriptionDto.Response subscribe(String email) {
		return subscriptionRestClient.subscribe(email).getBody();
	}
}
