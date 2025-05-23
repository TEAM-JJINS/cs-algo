package kr.co.csalgo.domain.auth.service;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@DisplayName("VerificationCodeService Test")
@ExtendWith(MockitoExtension.class)
public class VerificationCodeServiceTest {
    @Mock
    private VerificationCodeRepository verificationCodeRepository;
    private VerificationCodeService verificationCodeService;

    @BeforeEach
    void setUp() {
        verificationCodeService = new VerificationCodeService(verificationCodeRepository);
    }

    @Test
    @DisplayName("인증코드가 정상적으로 생성되고 저장된다.")
    void testCreateSuccess() {
        String email = "test@example.com";
        VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

        verificationCodeService.create(email, type);

        verify(verificationCodeRepository).create(eq(email), anyString(), eq(type));
    }

    @Test
    @DisplayName("저장 실패 시 예외가 발생한다.")
    void testSaveFail() {
        doThrow(new RuntimeException("레포지토리 저장 실패")).when(verificationCodeRepository)
                .create(any(), any(), any());

        CustomBusinessException exception = assertThrows(CustomBusinessException.class, () ->
                verificationCodeService.create("fail@example.com", VerificationCodeType.SUBSCRIPTION));

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }
}