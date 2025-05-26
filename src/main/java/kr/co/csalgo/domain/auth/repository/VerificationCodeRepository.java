package kr.co.csalgo.domain.auth.repository;

import kr.co.csalgo.domain.auth.type.VerificationCodeType;

public interface VerificationCodeRepository {
	void create(String key, String code, VerificationCodeType verificationCodeType);
}
