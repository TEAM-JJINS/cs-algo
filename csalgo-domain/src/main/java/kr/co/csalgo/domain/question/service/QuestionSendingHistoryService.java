package kr.co.csalgo.domain.question.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.entity.QuestionSendingHistory;
import kr.co.csalgo.domain.question.repository.QuestionSendingHistoryRepository;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;
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

	public boolean isSent(Long userId, LocalDate date) {
		User user = userService.read(userId);
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
		return questionSendingHistoryRepository.existsByUserAndCreatedAtBetween(user, startOfDay, endOfDay);
	}

	public long count(User user) {
		return questionSendingHistoryRepository.countByUser(user);
	}
}
