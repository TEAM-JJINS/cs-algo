package kr.co.csalgo.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.csalgo.domain.question.entity.QuestionSendingHistory;
import kr.co.csalgo.domain.user.entity.User;

public interface QuestionSendingHistoryRepository extends JpaRepository<QuestionSendingHistory, Long> {
	long countByUser(User user);
}
