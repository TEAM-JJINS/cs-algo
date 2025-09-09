package kr.co.csalgo.domain.question.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.csalgo.domain.question.entity.ResponseFeedback;
import kr.co.csalgo.domain.question.entity.ResponseFeedbackSendingHistory;

public interface ResponseFeedbackSendingHistoryRepository
	extends JpaRepository<ResponseFeedbackSendingHistory, Long> {
	boolean existsByResponseFeedbackAndCreatedAtBetween(ResponseFeedback responseFeedback, LocalDateTime start, LocalDateTime end);
}
