package kr.co.csalgo.web.unsubscription.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import kr.co.csalgo.web.unsubscription.dto.UnsubscriptionDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UnsubscriptionRestClient {

	private final RestClient restClient;

	public ResponseEntity<?> unsubscribe(Long userId) {
		try {
			return restClient.delete()
				.uri("/subscriptions", builder -> builder.queryParam("userId", userId).build())
				.retrieve()
				.toEntity(UnsubscriptionDto.Response.class);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
