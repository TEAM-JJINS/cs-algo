package kr.co.csalgo.web.unsubscription.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.csalgo.web.unsubscription.service.UnsubscriptionService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/unsubscription")
@RequiredArgsConstructor
public class UnsubscriptionWebController {

	private final UnsubscriptionService unsubscriptionService;

	@GetMapping
	public String showUnsubscriptionPage(@RequestParam("userId") Long userId, Model model) {
		model.addAttribute("userId", userId);
		return "unsubscription/unsubscription-page";
	}

	@DeleteMapping
	public ResponseEntity<?> unsubscribe(@RequestParam("userId") Long userId) {
		return unsubscriptionService.unsubscribe(userId);
	}
}
