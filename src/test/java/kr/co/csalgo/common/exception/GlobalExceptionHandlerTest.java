package kr.co.csalgo.common.exception;

import kr.co.csalgo.presentation.exception.TestExceptionController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestExceptionController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    @DisplayName("비즈니스 예외 처리 - USER_NOT_FOUND")
    void testBusinessException() throws Exception {
        mockMvc.perform(get("/test/business"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("IllegalArgumentException 처리")
    void testIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/test/illegal"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    @DisplayName("Validation 실패 처리")
    void testValidationError() throws Exception {
        String invalidJson = """
                { "email": "" }
                """;

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("JSON 파싱 실패 처리")
    void testMessageNotReadable() throws Exception {
        String malformedJson = "{ email: \"test@example.com\" }"; // key에 따옴표 없음

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.MESSGAE_NOT_READABLE.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.MESSGAE_NOT_READABLE.getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
