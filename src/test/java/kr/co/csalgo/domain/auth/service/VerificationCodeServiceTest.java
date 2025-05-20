package kr.co.csalgo.domain.auth.service;

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
import static org.mockito.Mockito.*;

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

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                verificationCodeService.create("fail@example.com", VerificationCodeType.SUBSCRIPTION));

        assertEquals("인증 코드 저장에 실패했습니다.", exception.getMessage());
    }
}