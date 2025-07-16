package kr.co.csalgo.domain.question.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import kr.co.csalgo.domain.common.entity.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE response_feedback SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ResponseFeedback extends AuditableEntity {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "response_id", nullable = false)
	private QuestionResponse response;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Builder
	public ResponseFeedback(QuestionResponse response, String content) {
		this.response = response;
		this.content = content;
	}
}
