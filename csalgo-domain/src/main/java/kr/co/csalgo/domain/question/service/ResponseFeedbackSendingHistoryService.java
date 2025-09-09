package kr.co.csalgo.domain.question.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

import kr.co.csalgo.domain.question.entity.ResponseFeedback;
import kr.co.csalgo.domain.question.entity.ResponseFeedbackSendingHistory;
import kr.co.csalgo.domain.question.repository.ResponseFeedbackSendingHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseFeedbackSendingHistoryService {
	private final ResponseFeedbackSendingHistoryRepository responseFeedbackSendingHistoryRepository;

	public void create(ResponseFeedback responseFeedback) {
		ResponseFeedbackSendingHistory history = ResponseFeedbackSendingHistory.builder()
			.responseFeedback(responseFeedback)
			.build();
		responseFeedbackSendingHistoryRepository.save(history);
	}

	public boolean isSent(ResponseFeedback responseFeedback, LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
		return responseFeedbackSendingHistoryRepository.existsByResponseFeedbackAndCreatedAtBetween(responseFeedback, startOfDay, endOfDay);
	}
}
