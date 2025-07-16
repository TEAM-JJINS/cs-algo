package kr.co.csalgo.domain.question.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kr.co.csalgo.question.entity.QuestionResponse;
import kr.co.csalgo.question.entity.ResponseFeedback;
import kr.co.csalgo.question.repository.ResponseFeedbackRepository;
import kr.co.csalgo.question.service.ResponseFeedbackService;

class ResponseFeedbackServiceTest {

	@Mock
	private ResponseFeedbackRepository responseFeedbackRepository;

	@InjectMocks
	private ResponseFeedbackService responseFeedbackService;

	public ResponseFeedbackServiceTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("피드백이 정상적으로 생성되어 저장된다")
	void createFeedbackSuccessfully() {
		// given
		QuestionResponse response = mock(QuestionResponse.class);
		String content = "개선 사항입니다.";

		ResponseFeedback expectedFeedback = ResponseFeedback.builder()
			.response(response)
			.content(content)
			.build();

		when(responseFeedbackRepository.save(any())).thenReturn(expectedFeedback);

		// when
		ResponseFeedback result = responseFeedbackService.create(response, content);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getResponse()).isEqualTo(response);
		assertThat(result.getContent()).isEqualTo(content);
		verify(responseFeedbackRepository).save(any(ResponseFeedback.class));
	}

	@Test
	@DisplayName("피드백 존재 여부를 정확하게 반환한다")
	void checkIfFeedbackExists() {
		// given
		QuestionResponse response = mock(QuestionResponse.class);
		when(responseFeedbackRepository.existsByResponse(response)).thenReturn(true);

		// when
		boolean exists = responseFeedbackService.isFeedbackExists(response);

		// then
		assertThat(exists).isTrue();
		verify(responseFeedbackRepository).existsByResponse(response);
	}
}
