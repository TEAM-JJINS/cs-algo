package kr.co.csalgo.domain.question.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.repository.QuestionRepository;

@DisplayName("QuestionService Test")
@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

	@Mock
	private QuestionRepository questionRepository;

	private QuestionService questionService;

	@BeforeEach
	void setUp() {
		questionService = new QuestionService(questionRepository);
	}

	@Test
	@DisplayName("존재하는 ID로 질문을 조회할 수 있다.")
	void testReadQuestionSuccess() {
		// given
		Long id = 1L;
		Question question = Question.builder()
			.title("Sample Question")
			.description("This is a sample question description.")
			.solution("Sample solution code here.")
			.build();

		when(questionRepository.findById(id)).thenReturn(Optional.of(question));

		// when
		Question result = questionService.read(id);

		// then
		assertNotNull(result);
		assertEquals("Sample Question", result.getTitle());
		verify(questionRepository).findById(id);
	}

	@Test
	@DisplayName("존재하지 않는 ID로 질문 조회 시 예외가 발생한다.")
	void testReadQuestionNotFound() {
		// given
		Long id = 999L;
		when(questionRepository.findById(id)).thenReturn(Optional.empty());

		// when & then
		CustomBusinessException exception = assertThrows(CustomBusinessException.class, () -> {
			questionService.read(id);
		});
		assertEquals(ErrorCode.QUESTION_NOT_FOUND, exception.getErrorCode());
		verify(questionRepository).findById(id);
	}
}
