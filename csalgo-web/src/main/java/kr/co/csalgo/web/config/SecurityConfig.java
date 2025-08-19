package kr.co.csalgo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import kr.co.csalgo.web.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable) // API 서버라면 보통 disable
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
				.requestMatchers("/admin/login").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll()
			).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			// formLogin 제거 (우리가 직접 API 구현했음)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.permitAll()
			);

		return http.build();
	}
}
