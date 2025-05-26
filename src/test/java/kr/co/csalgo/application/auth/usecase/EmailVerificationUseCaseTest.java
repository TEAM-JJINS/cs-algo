package kr.co.csalgo.application.auth.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.auth.service.VerificationCodeService;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import kr.co.csalgo.infrastructure.email.service.EmailService;

@DisplayName("EmailVerificationUseCase Test")
@ExtendWith(MockitoExtension.class)
public class EmailVerificationUseCaseTest {
	@Mock
	private VerificationCodeService verificationCodeService;
	@Mock
	private EmailService emailService;
	private EmailVerificationUseCase emailVerificationUseCase;

	@BeforeEach
	void setUp() {
		emailVerificationUseCase = new EmailVerificationUseCase(verificationCodeService, emailService);
	}

	@Test
	@DisplayName("이메일 인증번호 요청 테스트")
	void testCreateVerificationCodeSuccess() {
		String email = "test@example.com";
		String code = "123456";
		VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

		EmailVerificationCodeDto.Request request = EmailVerificationCodeDto.Request.builder()
			.email(email)
			.type(type)
			.build();

		when(verificationCodeService.create(email, type)).thenReturn(code);

		EmailVerificationCodeDto.Response response = emailVerificationUseCase.sendEmailVerificationCode(request);

		verify(verificationCodeService).create(email, type);
		verify(emailService).sendEmail(email, code);
		assertEquals(MessageCode.EMAIL_SENT_SUCCESS.getMessage(), response.getMessage());
	}

}
