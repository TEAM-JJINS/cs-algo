package kr.co.csalgo.web.subscribe;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/unsubscribe")
public class UnSubscribeWebController {

	@GetMapping
	public String showUnSubscribePage() {
		return "unsubscribe/unsubscribe-page";
	}
}
