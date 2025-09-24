package kr.co.csalgo.domain.question.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;

	public Question read(Long id) {
		return questionRepository.findById(id)
			.orElseThrow(() -> new CustomBusinessException(ErrorCode.QUESTION_NOT_FOUND));
	}

	public Question read(String title) {
		return questionRepository.findByTitle(title)
			.orElseThrow(() -> new CustomBusinessException(ErrorCode.QUESTION_NOT_FOUND));
	}

	public Page<Question> list(Pageable pageable) {
		return questionRepository.findAll(pageable);
	}

	public List<Question> list() {
		return questionRepository.findAll();
	}

	@Transactional
	public void update(Long id, String title, String solution, String description) {
		Question question = this.read(id);
		question.updateQuestion(title, solution, description);
	}

	@Transactional
	public void delete(Long id) {
		Question question = this.read(id);
		questionRepository.delete(question);
	}

}
