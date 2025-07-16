package kr.co.csalgo.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.csalgo.question.entity.QuestionSendingHistory;
import kr.co.csalgo.user.entity.User;

public interface QuestionSendingHistoryRepository extends JpaRepository<QuestionSendingHistory, Long> {
	long countByUser(User user);
}
