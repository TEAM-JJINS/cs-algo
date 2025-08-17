package kr.co.csalgo.domain.auth.service;

import org.springframework.stereotype.Service;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.auth.entity.AuthCredential;
import kr.co.csalgo.domain.auth.port.PasswordHasher;
import kr.co.csalgo.domain.auth.repository.AuthCredentialRepository;
import kr.co.csalgo.domain.auth.type.CredentialType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthCredentialService {
	private final AuthCredentialRepository authCredentialRepository;
	private final PasswordHasher passwordHasher;

	public AuthCredential read(Long userId, CredentialType type) {
		return authCredentialRepository.findByUserIdAndType(userId, type)
			.orElseThrow(() -> new CustomBusinessException(ErrorCode.CREDENTIAL_NOT_FOUND));
	}

	public boolean matches(String rawPassword, String passwordHash) {
		return passwordHasher.matches(rawPassword, passwordHash);
	}
}
