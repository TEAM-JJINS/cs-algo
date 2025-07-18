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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
		// given
		Question question1 = new Question("문제1", "설명1", "풀이1");
		Question question2 = new Question("문제2", "설명2", "풀이2");

		Page<Question> page = new PageImpl<>(List.of(question1, question2), PageRequest.of(0, 2), 10);
		when(questionService.list(0, 2)).thenReturn(page);

		// when
		Page<QuestionDto.Response> result = getQuestionUseCase.getQuestionListWithPaging(0, 2);

		// then
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent().get(0).getTitle()).isEqualTo("문제1");
		assertThat(result.getContent().get(1).getTitle()).isEqualTo("문제2");
	}
}
