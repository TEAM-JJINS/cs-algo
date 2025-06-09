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
import kr.co.csalgo.application.auth.dto.EmailVerificationVerifyDto;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.common.util.MailTemplate;
import kr.co.csalgo.domain.auth.service.VerificationCodeService;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import kr.co.csalgo.domain.email.EmailSender;

@DisplayName("EmailVerificationUseCase Test")
@ExtendWith(MockitoExtension.class)
public class EmailVerificationUseCaseTest {
	@Mock
	private VerificationCodeService verificationCodeService;
	@Mock
	private EmailSender emailSender;
	private EmailVerificationUseCase emailVerificationUseCase;

	@BeforeEach
	void setUp() {
		emailVerificationUseCase = new EmailVerificationUseCase(verificationCodeService, emailSender);
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
		verify(emailSender).send(
			email,
			MailTemplate.VERIFICATION_CODE_SUBJECT,
			MailTemplate.formatVerificationCodeBody(code)
		);
		assertEquals(MessageCode.EMAIL_SENT_SUCCESS.getMessage(), response.getMessage());
	}

	@Test
	@DisplayName("이메일 인증코드 검증 테스트")
	void testVerifyVerifcationCodeSuccess() {
		EmailVerificationVerifyDto.Request request = EmailVerificationVerifyDto.Request.builder()
			.email("team.jjins@gmail.com")
			.code("123456")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		when(verificationCodeService.verify(request.getEmail(), request.getCode(), request.getType()))
			.thenReturn(true);

		EmailVerificationVerifyDto.Response response = emailVerificationUseCase.verifyEmailVerificationCode(request);

		verify(verificationCodeService).verify(request.getEmail(), request.getCode(), request.getType());
		assertEquals(MessageCode.EMAIL_VERIFICATION_SUCCESS.getMessage(), response.getMessage());
	}
}
