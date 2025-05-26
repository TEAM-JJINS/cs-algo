package kr.co.csalgo.domain.auth.service;

import org.springframework.stereotype.Service;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.auth.generator.VerificationCodeGenerator;
import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
	private final VerificationCodeRepository verificationCodeRepository;
	private final VerificationCodeGenerator verificationCodeGenerator;

	public String create(String email, VerificationCodeType verificationCodeType) {
		String code = verificationCodeGenerator.generate();
		saveVerificationCode(email, code, verificationCodeType);
		return code;
	}

	public boolean verify(String email, String code, VerificationCodeType verificationCodeType) {
		boolean isValid = verificationCodeRepository.verify(email, code, verificationCodeType);
		if (!isValid) {
			throw new CustomBusinessException(ErrorCode.VERIFICATION_CODE_MISMATCH);
		}
		return true;
	}

	private void saveVerificationCode(String email, String code, VerificationCodeType verificationCodeType) {
		try {
			verificationCodeRepository.create(email, code, verificationCodeType);
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

}
