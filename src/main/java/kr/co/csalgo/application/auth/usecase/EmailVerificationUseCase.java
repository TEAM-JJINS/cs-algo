package kr.co.csalgo.application.auth.usecase;

import org.springframework.stereotype.Component;

import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.application.auth.dto.EmailVerificationVerifyDto;
import kr.co.csalgo.domain.auth.service.VerificationCodeService;
import kr.co.csalgo.infrastructure.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationUseCase {
	private final VerificationCodeService verificationCodeService;
	private final EmailService emailService;

	public EmailVerificationCodeDto.Response sendEmailVerificationCode(EmailVerificationCodeDto.Request request) {
		String email = request.getEmail();
		String type = request.getType().name();

		log.info("[이메일 인증 코드 발송 시작] email={}, type={}", email, type);

		String code = verificationCodeService.create(email, request.getType());

		log.debug("인증 코드 생성 완료: email={}, code={}", email, code);

		emailService.sendEmail(
			email,
			"CS-ALGO 인증 코드",
			"<h3>인증 코드</h3><p>" + code + "</p>");

		log.info("[이메일 인증 코드 발송 완료] email={}", email);

		return EmailVerificationCodeDto.Response.of();
	}

	public EmailVerificationVerifyDto.Response verifyEmailVerificationCode(
		EmailVerificationVerifyDto.Request request) {

		log.info("[이메일 인증 코드 검증 시작] email={}, type={}", request.getEmail(), request.getType());

		verificationCodeService.verify(request.getEmail(), request.getCode(), request.getType());

		log.info("[이메일 인증 코드 검증 성공] email={}, type={}", request.getEmail(), request.getType());

		return EmailVerificationVerifyDto.Response.of();
	}

}
