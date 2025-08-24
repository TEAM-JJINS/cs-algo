package kr.co.csalgo.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import kr.co.csalgo.infrastructure.security.CustomAccessDeniedHandler;
import kr.co.csalgo.infrastructure.security.CustomAuthenticationEntryPoint;
import kr.co.csalgo.infrastructure.security.JwtAuthenticationFilter;
import kr.co.csalgo.infrastructure.security.JwtProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	private final JwtProvider jwtProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@SuppressWarnings("squid:S4502")
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationEntryPoint authenticationEntryPoint,
		AccessDeniedHandler accessDeniedHandler) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
				.requestMatchers("/api/users/**").hasRole("ADMIN")
				// Swagger 허용
				.requestMatchers(
					"/swagger-ui/**",
					"/v3/api-docs/**",
					"/swagger-resources/**",
					"/webjars/**"
				).permitAll()
				// Actuator 허용
				.requestMatchers(
					"/actuator/**"
				).permitAll()
				// 구독 API 허용
				.requestMatchers(
					"/api/subscriptions/**"
				).permitAll()
				// 인증 관련 API 허용
				.requestMatchers(
					"/api/auth/**"
				).permitAll()
				// 관리자 인증 관련 API 허용
				.requestMatchers(
					"/api/admin/**"
				).permitAll()
				// 그 외 모든 요청은 인증 필요
				.anyRequest().authenticated()
			)
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				.accessDeniedHandler(new CustomAccessDeniedHandler())
			)
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
			.build();
	}
}
