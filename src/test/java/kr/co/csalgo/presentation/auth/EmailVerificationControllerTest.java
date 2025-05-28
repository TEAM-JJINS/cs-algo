package kr.co.csalgo.presentation.auth;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.application.auth.dto.EmailVerificationVerifyDto;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.auth.service.VerificationCodeService;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import kr.co.csalgo.infrastructure.email.service.EmailService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmailVerificationControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockitoBean
	private VerificationCodeService verificationCodeService;

	@MockitoBean
	private EmailService emailService;

	@BeforeEach
	void setUp() {
		doNothing().when(emailService).sendEmail(anyString(), anyString());
	}

	@Test
	@DisplayName("사용자는 정상 이메일로 인증번호를 받을 수 있다。")
	void testRequestSendEmailVerificationCodeSuccess() throws Exception {
		EmailVerificationCodeDto.Request request = EmailVerificationCodeDto.Request.builder()
			.email("team.jjins@gmail.com")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		when(verificationCodeService.create(anyString(), any()))
			.thenReturn("123456");

		mockMvc.perform(post("/api/auth/email-verifications/request")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 같은 이메일로 5분 내 재요청해도 정상적으로 받을 수 있다。")
	void testRequestResendVerificationCodeWithin5Minutes() throws Exception {
		EmailVerificationCodeDto.Request request = EmailVerificationCodeDto.Request.builder()
			.email("team.jjins@gmail.com")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		when(verificationCodeService.create(anyString(), any()))
			.thenReturn("123456");

		mockMvc.perform(post("/api/auth/email-verifications/request")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk());

		mockMvc.perform(post("/api/auth/email-verifications/request")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("이메일이 공백이면 인증번호를 받을 수 없다.")
	void testRequestFailWhenEmailIsBlank() throws Exception {
		EmailVerificationCodeDto.Request request = EmailVerificationCodeDto.Request.builder()
			.email("")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/request")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("올바르지 않은 이메일 형식으로는 인증번호를 받을 수 없다.")
	void testRequestFailWhenEmailFormatIsInvalid() throws Exception {
		EmailVerificationCodeDto.Request request = EmailVerificationCodeDto.Request.builder()
			.email("team-jjinsgmail.com")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/request")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("인증 type이 공백이면 인증번호를 받을 수 없다.")
	void testRequestFailWhenVerificationCodeTypeIsNull() throws Exception {
		EmailVerificationCodeDto.Request request = EmailVerificationCodeDto.Request.builder()
			.email("team.jjins@gmail.com")
			.type(null)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/request")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("이메일 검증 코드를 입력하여 인증을 완료할 수 있다.")
	void testVerifyEmailCodeSuccess() throws Exception {
		String email = "team.jjins@gmail.com";
		String code = "123456";

		when(verificationCodeService.create(anyString(), any())).thenReturn(code);
		when(verificationCodeService.verify(anyString(), anyString(), any())).thenReturn(true);

		// 인증 코드 요청
		EmailVerificationCodeDto.Request codeRequest = EmailVerificationCodeDto.Request.builder()
			.email(email)
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/request")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(codeRequest)))
			.andExpect(status().isOk())
			.andDo(print());

		// 인증 코드 검증 요청
		EmailVerificationVerifyDto.Request verifyRequest = EmailVerificationVerifyDto.Request.builder()
			.email(email)
			.code(code)
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(verifyRequest)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("이메일 검증 코드 입력 시 이메일이 공백이면 인증을 완료할 수 없다.")
	void testVerifyFailWhenEmailIsBlank() throws Exception {
		EmailVerificationVerifyDto.Request request = EmailVerificationVerifyDto.Request.builder()
			.email("")
			.code("123456")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("이메일 검증 코드 입력 시 코드가 없다면 인증을 완료할 수 없다.")
	void testVerifyFailWhenCodeIsBlank() throws Exception {
		EmailVerificationVerifyDto.Request request = EmailVerificationVerifyDto.Request.builder()
			.email("team.jjins@gmail.com")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("이메일 검증 코드 입력 시 인증 type이 공백이면 인증을 완료할 수 없다.")
	void testVerifyFailWhenTypeIsBlank() throws Exception {
		EmailVerificationVerifyDto.Request request = EmailVerificationVerifyDto.Request.builder()
			.email("")
			.code("123456")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("올바르지 않은 이메일 형식으로는 인증번호를 받을 수 없다.")
	void testVerifyFailWhenEmailFormatIsInvalid() throws Exception {
		EmailVerificationVerifyDto.Request request = EmailVerificationVerifyDto.Request.builder()
			.email("team-jjinsgmail.com")
			.code("123456")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 이미 검증된 이메일로 재검증을 받을 수 없다.")
	void testVerifyFailWhenResend() throws Exception {
		String email = "team.jjins@gmail.com";
		String code = "123456";

		when(verificationCodeService.create(anyString(), any())).thenReturn(code);
		when(verificationCodeService.verify(anyString(), anyString(), any()))
			.thenReturn(true) // 첫 요청은 성공
			.thenThrow(new CustomBusinessException(ErrorCode.VERIFICATION_CODE_MISMATCH)); // 두 번째 요청은 실패

		// 인증 코드 요청
		EmailVerificationCodeDto.Request codeRequest = EmailVerificationCodeDto.Request.builder()
			.email(email)
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/request")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(codeRequest)))
			.andExpect(status().isOk());

		// 검증 요청 1: 성공
		EmailVerificationVerifyDto.Request verifyRequest = EmailVerificationVerifyDto.Request.builder()
			.email(email)
			.code(code)
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(verifyRequest)))
			.andExpect(status().isOk());

		// 검증 요청 2: 실패 (예외 발생)
		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(verifyRequest)))
			.andExpect(status().isBadRequest()) // 예외 처리됨
			.andExpect(jsonPath("$.code").value("B003")) // 예외 내용까지 확인 가능
			.andExpect(jsonPath("$.message").value("인증 코드가 일치하지 않습니다."));

		// verify 호출 횟수 검증
		verify(verificationCodeService, times(2)).verify(anyString(), anyString(), any());
	}
}
