package kr.co.csalgo.domain.question.service;

import org.springframework.stereotype.Service;

import kr.co.csalgo.domain.question.entity.QuestionSendingHistory;
import kr.co.csalgo.domain.question.repository.QuestionSendingHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionSendingHistoryService {
	private final QuestionSendingHistoryRepository questionSendingHistoryRepository;

	public QuestionSendingHistory create(QuestionSendingHistory questionSendingHistory) {
		// TODO: 1. 문제 존재 여부 검증
		// TODO: 2. 유저 존재 여부 검증
		return questionSendingHistoryRepository.save(questionSendingHistory);
	}
}
