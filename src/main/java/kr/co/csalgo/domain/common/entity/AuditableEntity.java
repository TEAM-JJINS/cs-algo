package kr.co.csalgo.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@SQLDelete(sql = "UPDATE #{entityName} SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity extends IdentifiableEntity {

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;
}
