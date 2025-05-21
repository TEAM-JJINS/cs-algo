package kr.co.csalgo.presentation.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Email Verification", description = "이메일 인증코드 관련 API")
public class EmailVerificationController {
    private final EmailVerificationUseCase emailVerificationUseCase;

    @PostMapping("/request")
    @Operation(summary = "인증코드 요청", description = "사용자가 이메일을 통해 인증코드를 요청 및 수신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증코드 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)"),
    })
    public ResponseEntity<?> sendEmailVerificationCode(@Valid @RequestBody EmailVerificationCodeDto.Request request) {
        return ResponseEntity.ok(emailVerificationUseCase.sendEmailVerificationCode(request));
    }
}
