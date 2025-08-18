package kr.co.csalgo.domain.auth.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.csalgo.domain.auth.type.CredentialType;
import kr.co.csalgo.domain.common.entity.AuditableEntity;
import kr.co.csalgo.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class AuthCredential extends AuditableEntity {
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	private CredentialType type;

	@Column(length = 200)
	private String passwordHash;

	@Builder
	public AuthCredential(User user, CredentialType type, String passwordHash) {
		this.user = user;
		this.type = type;
		this.passwordHash = passwordHash;
	}
}
