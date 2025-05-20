package kr.co.csalgo.presentation.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kr.co.csalgo.application.auth.dto.EmailVerificationCodeDto;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void testSendEmailVerificationCodeSuccess() throws Exception {
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
    void testResend() throws Exception {
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
    void testBlankEmail() throws Exception {
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
    void testInvalidateEmailFormat() throws Exception {
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
    void testBlankVerificationCodeType() throws Exception {
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
}
