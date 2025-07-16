package kr.co.csalgo.auth.repository;

import kr.co.csalgo.auth.type.VerificationCodeType;

public interface VerificationCodeRepository {
	void create(String email, String code, VerificationCodeType verificationCodeType);

	boolean verify(String email, String code, VerificationCodeType verificationCodeType);

	default String generateKey(VerificationCodeType verificationCodeType, String email) {
		return verificationCodeType + "::" + email;
	}
}
