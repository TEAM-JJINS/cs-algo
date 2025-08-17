package kr.co.csalgo.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.csalgo.domain.auth.entity.AuthCredential;
import kr.co.csalgo.domain.auth.type.CredentialType;

public interface AuthCredentialRepository extends JpaRepository<AuthCredential, Long> {
	Optional<AuthCredential> findByUserIdAndType(Long userId, CredentialType type);
}
