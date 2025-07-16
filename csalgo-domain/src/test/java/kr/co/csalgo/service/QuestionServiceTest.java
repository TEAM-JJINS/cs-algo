package kr.co.csalgo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.question.entity.Question;
import kr.co.csalgo.question.repository.QuestionRepository;
import kr.co.csalgo.question.service.QuestionService;

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

	@Test
	@DisplayName("모든 질문을 조회할 수 있다.")
	void testListQuestions() {

		Question question1 = Question.builder()
			.title("Question 1")
			.description("Desc 1")
			.solution("Solution 1")
			.build();

		Question question2 = Question.builder()
			.title("Question 2")
			.description("Desc 2")
			.solution("Solution 2")
			.build();

		when(questionRepository.findAll()).thenReturn(List.of(question1, question2));

		List<Question> result = questionService.list();

		assertEquals(2, result.size());
		assertEquals("Question 1", result.get(0).getTitle());
		assertEquals("Question 2", result.get(1).getTitle());
		verify(questionRepository).findAll();
	}

	@Test
	@DisplayName("제목을 통해 질문을 조회할 수 있다.")
	void testReadQuestionByTitleSuccess() {
		String title = "What is TDD?";
		Question question = Question.builder().title(title).build();
		when(questionRepository.findByTitle(title)).thenReturn(Optional.of(question));

		Question result = questionService.read(title);

		assertNotNull(result);
		assertEquals(title, result.getTitle());
		verify(questionRepository).findByTitle(title);
	}

	@Test
	@DisplayName("존재하지 않는 제목으로 질문을 조회할 경우 예외가 발생한다.")
	void testReadQuestionByTitleNotFound() {
		String title = "존재하지 않는 제목";
		when(questionRepository.findByTitle(title)).thenReturn(Optional.empty());

		assertThrows(CustomBusinessException.class, () -> questionService.read(title));
		verify(questionRepository).findByTitle(title);
	}
}
