package kr.co.csalgo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				// 공개 페이지
				.requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
				.requestMatchers("/api/public/**").permitAll()

				// 관리자 인증 필요한 영역
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/api/admin/**").hasRole("ADMIN")

				// 나머지 경로 처리
				.anyRequest().permitAll()
			)
			.formLogin(form -> form
				.loginPage("/admin/login")              // 로그인 UI 경로
				.loginProcessingUrl("/api/admin/login") // 로그인 처리 API
				.defaultSuccessUrl("/admin/dashboard", true)
				.failureUrl("/admin/login?error=true")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.permitAll()
			);
		return http.build();
	}
}
