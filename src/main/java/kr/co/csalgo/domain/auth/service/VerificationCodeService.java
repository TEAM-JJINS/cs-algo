package kr.co.csalgo.domain.auth.service;

import org.springframework.stereotype.Service;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.common.util.VerificationCodeUtil;
import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
	private final VerificationCodeRepository verificationCodeRepository;

	public String create(String email, VerificationCodeType verificationCodeType) {
		String code = VerificationCodeUtil.generate();
		saveVerificationCode(email, code, verificationCodeType);
		return code;
	}

	public boolean verify(String email, String code, VerificationCodeType verificationCodeType) {
		try {
			boolean isValid = verificationCodeRepository.verify(email, code, verificationCodeType);
			if (!isValid) {
				throw new CustomBusinessException(ErrorCode.VERIFICATION_CODE_MISMATCH);
			}
			return true;
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private void saveVerificationCode(String email, String code, VerificationCodeType verificationCodeType) {
		try {
			verificationCodeRepository.create(email, code, verificationCodeType);
		} catch (Exception e) {
			throw new CustomBusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

}
