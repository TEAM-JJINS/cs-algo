package kr.co.csalgo.domain.auth.service;

import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final VerificationCodeRepository verificationCodeRepository;

    public void save(String email, String code, VerificationCodeType verificationCodeType) {
        verificationCodeRepository.create(email, code, verificationCodeType);
    }
}
