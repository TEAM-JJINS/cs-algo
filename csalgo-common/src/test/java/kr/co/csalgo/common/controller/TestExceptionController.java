package kr.co.csalgo.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;

@RestController
@RequestMapping("/test")
public class TestExceptionController {

	@GetMapping("/business")
	public void throwBusinessException() {
		throw new CustomBusinessException(ErrorCode.USER_NOT_FOUND);
	}

	@GetMapping("/illegal")
	public void throwIllegalArgumentException() {
		throw new IllegalArgumentException("잘못된 파라미터입니다.");
	}

	@PostMapping("/validation")
	public void triggerValidation(@Valid @RequestBody TestDto dto) {
		// validation 자동 발생
	}

	@GetMapping("/unknown")
	public void throwUnknownException() throws Exception {
		throw new Exception("예기치 못한 에러입니다.");
	}

	public static class TestDto {
		@NotBlank(message = "email은 필수입니다.")
		private String email;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}
}

