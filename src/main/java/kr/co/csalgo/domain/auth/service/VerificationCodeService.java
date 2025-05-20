package kr.co.csalgo.domain.auth.service;

import kr.co.csalgo.common.util.VerificationCodeUtil;
import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final VerificationCodeRepository verificationCodeRepository;

    public String create(String email, VerificationCodeType verificationCodeType) {
        String code = VerificationCodeUtil.generate();
        saveVerificationCode(email, code, verificationCodeType);
        return code;
    }

    private void saveVerificationCode(String email, String code, VerificationCodeType verificationCodeType) {
        try {
            verificationCodeRepository.create(email, code, verificationCodeType);
        } catch (Exception e) {
            throw new IllegalStateException("인증 코드 저장에 실패했습니다.");
        }
    }

}
