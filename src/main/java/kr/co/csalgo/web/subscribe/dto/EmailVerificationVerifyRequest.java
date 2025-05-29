package kr.co.csalgo.web.subscribe.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailVerificationVerifyRequest {
	private String email;
	private String code;
	private VerificationCodeType type;
}
