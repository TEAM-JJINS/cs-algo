package kr.co.csalgo.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.csalgo.domain.question.entity.QuestionSendingHistory;

public interface QuestionSendingHistoryRepository extends JpaRepository<QuestionSendingHistory, Long> {
}
