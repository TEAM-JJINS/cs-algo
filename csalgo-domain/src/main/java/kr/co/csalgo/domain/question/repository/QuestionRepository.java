package kr.co.csalgo.domain.question.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.csalgo.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	Page<Question> findAll(Pageable pageable);

	Optional<Question> findById(Long id);

	Optional<Question> findByTitle(String title);
}
