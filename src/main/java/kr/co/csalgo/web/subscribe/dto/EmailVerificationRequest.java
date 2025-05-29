package kr.co.csalgo.web.subscribe.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailVerificationRequest {
	private String email;
	private VerificationCodeType type;
}
