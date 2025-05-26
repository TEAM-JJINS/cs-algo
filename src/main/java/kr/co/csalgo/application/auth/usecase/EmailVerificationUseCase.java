package kr.co.csalgo.application.auth.usecase;

import org.springframework.stereotype.Component;

import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.application.auth.dto.EmailVerificationVerifyDto;
import kr.co.csalgo.domain.auth.service.VerificationCodeService;
import kr.co.csalgo.infrastructure.email.service.EmailService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailVerificationUseCase {
	private final VerificationCodeService verificationCodeService;
	private final EmailService emailService;

	public EmailVerificationCodeDto.Response sendEmailVerificationCode(EmailVerificationCodeDto.Request request) {
		String code = verificationCodeService.create(request.getEmail(), request.getType());
		emailService.sendEmail(request.getEmail(), code);
		return EmailVerificationCodeDto.Response.of();
	}

	public EmailVerificationVerifyDto.Response verifyEmailVerificationCode(
		EmailVerificationVerifyDto.Request request) {
		verificationCodeService.verify(request.getEmail(), request.getCode(), request.getType());
		return EmailVerificationVerifyDto.Response.of();
	}
}
