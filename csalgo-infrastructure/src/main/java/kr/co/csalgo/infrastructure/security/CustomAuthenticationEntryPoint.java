package kr.co.csalgo.infrastructure.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.common.exception.ErrorResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");

		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED_ACCESS);
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}

