package kr.co.csalgo.infrastructure.email.config;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
public class MailProperties {
	private String host;
	private int port;
	private String username;
	private String password;
	private final Properties properties = new Properties();
}
