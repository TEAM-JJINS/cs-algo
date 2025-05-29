package kr.co.csalgo.web.subscribe.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.csalgo.web.subscribe.service.SubscriptionService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/subscribe")
@RequiredArgsConstructor
public class SubscribeController {
	private final SubscriptionService subscriptionService;

	@GetMapping
	public String mainPage() {
		return "index";
	}

	@PostMapping("/request-code")
	public ResponseEntity<Map<String, Object>> requestCode(@RequestParam String email) {
		String message = subscriptionService.emailVerificationRequest(email).getMessage();
		Map<String, Object> response = Map.of(
			"message", message
		);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/verify-code")
	public ResponseEntity<Map<String, Object>> verifyCode(@RequestParam String email, @RequestParam String code) {
		String message = subscriptionService.emailVerificationVerify(email, code).getMessage();
		Map<String, Object> response = Map.of(
			"message", message
		);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/subscribe")
	public ResponseEntity<Map<String, Object>> subscribe(@RequestParam String email) {
		String message = subscriptionService.subscribe(email).getMessage();
		Map<String, Object> response = Map.of(
			"message", message
		);
		return ResponseEntity.ok(response);
	}
}
