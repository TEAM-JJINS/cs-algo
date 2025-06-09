package kr.co.csalgo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import kr.co.csalgo.domain.email.EmailReceiver;
import kr.co.csalgo.domain.email.EmailSender;
import kr.co.csalgo.infrastructure.email.JavaEmailReceiver;
import kr.co.csalgo.infrastructure.email.JavaEmailSender;
import kr.co.csalgo.infrastructure.email.config.MailProperties;
import lombok.Getter;

@Configuration
@Getter
public class EmailInfraConfig {
	@Bean
	public JavaMailSender javaMailSender(MailProperties properties) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(properties.getHost());
		mailSender.setPort(properties.getPort());
		mailSender.setUsername(properties.getUsername());
		mailSender.setPassword(properties.getPassword());
		mailSender.setJavaMailProperties(properties.getProperties());
		return mailSender;
	}

	@Bean
	public EmailSender emailSender(JavaMailSender javaMailSender) {
		return new JavaEmailSender(javaMailSender);
	}

	@Bean
	public EmailReceiver emailReceiver(MailProperties properties) {
		return new JavaEmailReceiver(properties);
	}
}
