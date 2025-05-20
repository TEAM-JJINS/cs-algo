package kr.co.csalgo.domain.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final VerificationCodeRepository verificationCodeRepository;
    private final JavaMailSender mailSender;

    public void create(String email, VerificationCodeType verificationCodeType) {
        String code = generateVerificationCode();
        save(email, code, verificationCodeType);
        sendEmail(email, code);
    }

    private String generateVerificationCode() {
        SecureRandom secureRandom = new SecureRandom();
        int code = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    private void save(String email, String code, VerificationCodeType verificationCodeType) {
        try {
            verificationCodeRepository.create(email, code, verificationCodeType);
        } catch (Exception e) {
            throw new IllegalStateException("인증 코드 저장에 실패했습니다.");
        }
    }

    private void sendEmail(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("CS-ALGO 인증 코드");
            helper.setText("<h3>인증 코드</h3><p>" + code + "</p>", true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("이메일 전송에 실패했습니다.");
        }
    }
}
