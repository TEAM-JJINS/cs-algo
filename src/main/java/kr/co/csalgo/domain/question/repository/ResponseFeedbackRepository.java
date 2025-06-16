package kr.co.csalgo.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.csalgo.domain.question.entity.ResponseFeedback;

public interface ResponseFeedbackRepository extends JpaRepository<ResponseFeedback, Long> {
}
