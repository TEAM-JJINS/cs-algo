package kr.co.csalgo.presentation.auth;

import jakarta.validation.Valid;
import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.application.auth.usecase.EmailVerificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/email-verifications")
public class EmailVerificationController {
    private final EmailVerificationUseCase emailVerificationUseCase;

    @PostMapping("/request")
    public ResponseEntity<?> sendEmailVerificationCode(@Valid @RequestBody EmailVerificationCodeDto.Request request) {
        return ResponseEntity.ok(emailVerificationUseCase.sendEmailVerificationCode(request));
    }
}
