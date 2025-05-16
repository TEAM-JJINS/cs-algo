package kr.co.csalgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CsAlgoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsAlgoApplication.class, args);
    }

}
