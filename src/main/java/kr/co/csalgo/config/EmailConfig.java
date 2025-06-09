package kr.co.csalgo.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import kr.co.csalgo.domain.email.EmailReceiver;
import kr.co.csalgo.infrastructure.email.JavaMailReceiver;
import lombok.Getter;

@Configuration
@Getter
public class EmailConfig {

	@Value("${spring.mail.host}")
	private String mailHost;

	@Value("${spring.mail.port}")
	private int mailPort;

	@Value("${spring.mail.username}")
	private String mailUsername;

	@Value("${spring.mail.password}")
	private String mailPassword;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean starttls;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailHost);
		mailSender.setPort(mailPort);
		mailSender.setUsername(mailUsername);
		mailSender.setPassword(mailPassword);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.connection-timeout", "5000");
		props.put("mail.smtp.timeout", "5000");
		props.put("mail.smtp.write-timeout", "5000");

		return mailSender;
	}

	@Bean
	public EmailReceiver mailReceiver() {
		return new JavaMailReceiver(mailHost, mailUsername, mailPassword);
	}
}
