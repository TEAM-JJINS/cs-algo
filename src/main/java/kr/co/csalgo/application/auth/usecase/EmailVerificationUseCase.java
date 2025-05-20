package kr.co.csalgo.application.auth.usecase;


import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EmailVerificationUseCase {
    private final AuthService authService;

    public EmailVerificationCodeDto.Response sendEmailVerificationCode(EmailVerificationCodeDto.Request request) {
        authService.create(request.getEmail(), request.getType());
        return EmailVerificationCodeDto.Response.of();
    }
}
