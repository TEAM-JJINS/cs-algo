package kr.co.csalgo.presentation.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.application.auth.dto.EmailVerificationVerifyDto;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmailVerificationControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Test
	@DisplayName("사용자는 정상 이메일로 인증번호를 받을 수 있다。")
	void testRequestSendEmailVerificationCodeSuccess() throws Exception {
		EmailVerificationCodeDto.Request request = EmailVerificationCodeDto.Request.builder()
			.email("syjin9317@gmail.com")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

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
			.email("syjin9317@gmail.com")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

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
			.email("syjin9317@gmail.com")
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
		EmailVerificationVerifyDto.Request request = EmailVerificationVerifyDto.Request.builder()
			.email("team.jjins@gmail.com")
			.code("123456")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
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
	@DisplayName("이메일 검증 코드 입력 시 코드가 공백이면 인증을 완료할 수 없다.")
	void testVerifyFailWhenCodeIsBlank() throws Exception {
		EmailVerificationVerifyDto.Request request = EmailVerificationVerifyDto.Request.builder()
			.email("team.jjins@gmail.com")
			.code("")
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

		mockMvc.perform(post("/api/auth/email-verifications/request")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 이미 검증된 이메일로 재검증을 받을 수 없다.")
	void testVerifyFailWhenResend() throws Exception {
		EmailVerificationVerifyDto.Request request = EmailVerificationVerifyDto.Request.builder()
			.email("team.jjins@gmail.com")
			.code("123456")
			.type(VerificationCodeType.SUBSCRIPTION)
			.build();

		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(print());

		mockMvc.perform(post("/api/auth/email-verifications/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
}
