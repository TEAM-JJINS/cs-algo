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
				.requestMatchers("/admin/login", "/api/admin/login").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/admin/login")
				.loginProcessingUrl("/api/admin/login")
				.defaultSuccessUrl("/admin/dashboard", true)
				.failureUrl("/admin/login?error=true")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/admin/login?logout=true")
				.permitAll()
			)
			.securityContext(securityContext -> securityContext.requireExplicitSave(false));
		return http.build();
	}
}

