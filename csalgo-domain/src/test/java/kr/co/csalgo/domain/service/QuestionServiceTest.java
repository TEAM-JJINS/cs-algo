package kr.co.csalgo.domain.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.repository.QuestionRepository;
import kr.co.csalgo.domain.question.service.QuestionService;

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

	@Test
	@DisplayName("페이지 정보에 따라 질문 목록을 페이징하여 조회할 수 있다.")
	void testListQuestionsWithPaging() {
		// given
		int page = 0;
		int size = 2;
		Pageable pageable = PageRequest.of(page, size);

		Question q1 = Question.builder().title("Q1").description("D1").solution("S1").build();
		Question q2 = Question.builder().title("Q2").description("D2").solution("S2").build();
		Page<Question> mockPage = new PageImpl<>(List.of(q1, q2), pageable, 5);

		when(questionRepository.findAll(pageable)).thenReturn(mockPage);

		// when
		Page<Question> result = questionService.list(pageable);

		// then
		assertEquals(2, result.getContent().size());
		assertEquals("Q1", result.getContent().get(0).getTitle());
		verify(questionRepository).findAll(pageable);
	}

	@Test
	@DisplayName("문제 수정 성공")
	void testUpdateQuestionSuccess() {
		Long id = 1L;
		String newTitle = "수정된 제목";
		String newSolution = "수정된 풀이";

		Question question = new Question("기존 제목", "기존 설명", "기존 풀이");

		when(questionRepository.findById(id)).thenReturn(Optional.of(question));

		questionService.update(id, newTitle, newSolution);

		assertThat(question.getTitle()).isEqualTo(newTitle);
		assertThat(question.getSolution()).isEqualTo(newSolution);
	}

	@DisplayName("문제 삭제 성공")
	@Test
	void testDeleteQuestionSuccess() {
		Long id = 1L;
		Question question = new Question("기존 제목", "기존 설명", "기존 풀이");

		when(questionRepository.findById(id)).thenReturn(Optional.of(question));

		questionService.delete(id);

		verify(questionRepository, times(1)).delete(question);
	}

}
