package kr.co.csalgo.web.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrivacyPolicyWebController {
	@GetMapping("/privacy-policy")
	public String privacyPolicy() {
		return "privacy-policy";
	}
}
