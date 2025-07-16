package kr.co.csalgo.domain.question.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.repository.QuestionResponseRepository;
import kr.co.csalgo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionResponseService {
	private final QuestionResponseRepository questionResponseRepository;

	public QuestionResponse create(Question question, User user, String content, String messageId) {
		QuestionResponse questionResponse = QuestionResponse.builder()
			.question(question)
			.user(user)
			.content(content)
			.messageId(messageId)
			.build();
		questionResponseRepository.save(questionResponse);
		return questionResponse;
	}

	public List<QuestionResponse> list() {
		return questionResponseRepository.findAll();
	}
}
