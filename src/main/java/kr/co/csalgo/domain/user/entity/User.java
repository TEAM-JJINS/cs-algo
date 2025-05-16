package kr.co.csalgo.domain.user.entity;

import jakarta.persistence.*;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;


    @Builder
    public User(String email) {
        this.email = email;
    }
}
