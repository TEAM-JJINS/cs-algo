package kr.co.csalgo.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.csalgo.question.entity.QuestionResponse;
import kr.co.csalgo.question.entity.ResponseFeedback;

public interface ResponseFeedbackRepository extends JpaRepository<ResponseFeedback, Long> {
	boolean existsByResponse(QuestionResponse response);
}
