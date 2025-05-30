package kr.co.csalgo.web.unsubscription.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.csalgo.web.unsubscription.dto.UnsubscriptionDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UnsubscriptionRestClient {

	private final RestClient restClient;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public ResponseEntity<?> unsubscribe(Long userId) {
		try {
			return restClient.delete()
				.uri("/subscriptions", builder -> builder.queryParam("userId", userId).build())
				.exchange((req, res) -> {
					String body = res.bodyTo(String.class);

					if (res.getStatusCode().is2xxSuccessful()) {
						UnsubscriptionDto.Response dto = objectMapper.readValue(body, UnsubscriptionDto.Response.class);
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
