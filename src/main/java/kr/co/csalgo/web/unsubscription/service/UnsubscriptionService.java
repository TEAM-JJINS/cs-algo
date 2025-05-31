package kr.co.csalgo.web.unsubscription.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.csalgo.web.unsubscription.client.UnsubscriptionRestClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnsubscriptionService {

	private final UnsubscriptionRestClient unsubscriptionRestClient;

	public ResponseEntity<?> unsubscribe(Long userId) {
		ResponseEntity<?> response = unsubscriptionRestClient.unsubscribe(userId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			return response;
		}
		return ResponseEntity.ok(response.getBody());
	}
}

