package kr.co.csalgo.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import kr.co.csalgo.domain.common.entity.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User extends AuditableEntity {
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;


    @Builder
    public User(String email) {
        this.email = email;
    }
}
