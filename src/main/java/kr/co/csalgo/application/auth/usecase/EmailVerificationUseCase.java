package kr.co.csalgo.application.auth.usecase;


import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.domain.auth.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EmailVerificationUseCase {
    private final VerificationCodeService verificationCodeService;

    public EmailVerificationCodeDto.Response sendEmailVerificationCode(EmailVerificationCodeDto.Request request) {
        verificationCodeService.create(request.getEmail(), request.getType());
        return EmailVerificationCodeDto.Response.of();
    }
}
