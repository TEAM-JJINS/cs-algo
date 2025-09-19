package kr.co.csalgo.server.gpt;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.csalgo.infrastructure.gpt.GptClient;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("java:S2259")
public class GptController {
	private final GptClient gptClient;

	@GetMapping("/gpt-test")
	public String testGpt() {
		return gptClient.ask("안녕 GPT, 잘 연결됐어?");
	}
}
