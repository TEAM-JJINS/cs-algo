package kr.co.csalgo.web.subscribe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> subscribe(@RequestParam String email) {
		return subscriptionService.subscribe(email);
	}

	@PostMapping("/request-code")
	@ResponseBody
	public ResponseEntity<?> requestCode(@RequestParam String email) {
		return subscriptionService.emailVerificationRequest(email);
	}

	@PostMapping("/verify-code")
	@ResponseBody
	public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
		return subscriptionService.emailVerificationVerify(email, code);
	}
}
