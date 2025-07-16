package kr.co.csalgo.question.service;

import org.springframework.stereotype.Service;

import kr.co.csalgo.question.entity.Question;
import kr.co.csalgo.question.entity.QuestionSendingHistory;
import kr.co.csalgo.question.repository.QuestionSendingHistoryRepository;
import kr.co.csalgo.user.entity.User;
import kr.co.csalgo.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionSendingHistoryService {
	private final QuestionSendingHistoryRepository questionSendingHistoryRepository;
	private final QuestionService questionService;
	private final UserService userService;

	public QuestionSendingHistory create(Long questionId, Long userId) {
		Question question = questionService.read(questionId);

		User user = userService.read(userId);

		QuestionSendingHistory questionSendingHistory = QuestionSendingHistory.builder()
			.question(question)
			.user(user)
			.build();

		return questionSendingHistoryRepository.save(questionSendingHistory);
	}

	public long count(User user) {
		return questionSendingHistoryRepository.countByUser(user);
	}
}
