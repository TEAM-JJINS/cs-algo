package kr.co.csalgo.domain.user.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import kr.co.csalgo.domain.common.entity.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User extends AuditableEntity {

	@Column(nullable = false, length = 100)
	private String email;

	@Column(nullable = false, unique = true, columnDefinition = "BINARY(16)")
	private UUID uuid;

	@Column(nullable = false, length = 100)
	private String password;

	@Builder
	public User(String email, String password) {
		this.email = email;
		this.uuid = UUID.randomUUID();
		this.password = password;
	}
}
