package kr.co.csalgo.domain.question.service;

import org.springframework.stereotype.Service;

import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.entity.ResponseFeedback;
import kr.co.csalgo.domain.question.repository.ResponseFeedbackRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseFeedbackService {
	private final ResponseFeedbackRepository responseFeedbackRepository;

	public ResponseFeedback create(QuestionResponse response, String content) {
		ResponseFeedback feedback = ResponseFeedback.builder()
			.response(response)
			.content(content)
			.build();
		return responseFeedbackRepository.save(feedback);
	}

	public boolean isFeedbackExists(QuestionResponse response) {
		return responseFeedbackRepository.existsByResponse(response);
	}
}
