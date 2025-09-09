package kr.co.csalgo.domain.question.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.csalgo.domain.common.entity.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ResponseFeedbackSendingHistory extends AuditableEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "feedback_id")
	private ResponseFeedback feedback;

	@Builder
	public ResponseFeedbackSendingHistory(ResponseFeedback feedback, boolean success, String errorMessage) {
		this.feedback = feedback;
	}
}
