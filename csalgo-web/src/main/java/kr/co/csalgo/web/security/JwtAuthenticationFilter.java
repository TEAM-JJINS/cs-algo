package kr.co.csalgo.web.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtValidator jwtValidator;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		// 1. 쿠키에서 accessToken 추출
		String token = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
			.filter(c -> "accessToken".equals(c.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);

		if (token != null && jwtValidator.validateToken(token)) {
			// 2. 토큰에서 인증 정보 추출
			Authentication authentication = jwtValidator.getAuthentication(token);

			// 3. SecurityContext에 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}

