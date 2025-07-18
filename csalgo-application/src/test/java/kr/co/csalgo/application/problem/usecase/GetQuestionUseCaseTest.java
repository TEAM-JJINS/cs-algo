package kr.co.csalgo.application.problem.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.application.problem.dto.QuestionDto;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.service.QuestionService;

@DisplayName("GetQuestionUseCase 테스트")
@ExtendWith(MockitoExtension.class)
class GetQuestionUseCaseTest {

	@Mock
	private QuestionService questionService;

	@InjectMocks
	private GetQuestionUseCase getQuestionUseCase;

	@Test
	@DisplayName("문제 리스트 페이징 조회 테스트 성공")
	void getQuestionListWithPagingSuccess() {
		Question question1 = new Question("문제1", "설명1", "풀이1");
		Question question2 = new Question("문제2", "설명2", "풀이2");

		List<Question> questions = List.of(question1, question2);
		when(questionService.list(0, 2)).thenReturn(questions);

		List<QuestionDto.Response> result = getQuestionUseCase.getQuestionListWithPaging(0, 2);

		assertThat(result).hasSize(2);
		assertThat(result.get(0).getTitle()).isEqualTo("문제1");
		assertThat(result.get(1).getTitle()).isEqualTo("문제2");
	}
}
