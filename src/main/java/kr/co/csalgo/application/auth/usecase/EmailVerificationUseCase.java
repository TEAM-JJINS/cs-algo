package kr.co.csalgo.application.auth.usecase;


import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.domain.auth.service.VerificationCodeService;
import kr.co.csalgo.infrastructure.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


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
}
