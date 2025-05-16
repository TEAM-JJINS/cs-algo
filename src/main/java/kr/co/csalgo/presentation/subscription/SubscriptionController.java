package kr.co.csalgo.presentation.subscription;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.csalgo.application.user.dto.SubscriptionUseCaseDto;
import kr.co.csalgo.application.user.usecase.SubscriptionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription", description = "구독 관련 API")
public class SubscriptionController {
    private final SubscriptionUseCase subscriptionUseCase;

    @PostMapping
    @Operation(summary = "구독 등록", description = "사용자가 이메일을 통해 구독을 등록할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)"),
            @ApiResponse(responseCode = "409", description = "중복된 이메일로 인한 구독 불가")
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody SubscriptionUseCaseDto.Request request) {
        return ResponseEntity.ok(subscriptionUseCase.create(request));
    }
}
