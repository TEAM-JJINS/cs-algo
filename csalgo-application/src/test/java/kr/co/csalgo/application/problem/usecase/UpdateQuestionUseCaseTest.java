package kr.co.csalgo.application.problem.usecase;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.application.problem.dto.QuestionDto;
import kr.co.csalgo.domain.question.service.QuestionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateQuestionUseCase 테스트")
class UpdateQuestionUseCaseTest {

	@Mock
	private QuestionService questionService;

	@InjectMocks
	private UpdateQuestionUseCase updateQuestionUseCase;

	@Test
	@DisplayName("문제 수정 성공")
	void testUpdateQuestionSuccess() {
		Long questionId = 1L;
		QuestionDto.Request request = new QuestionDto.Request("수정된 제목", "수정된 풀이");

		doNothing().when(questionService).update(questionId, request.getTitle(), request.getSolution());

		String result = updateQuestionUseCase.updateQuestion(questionId, request);

		verify(questionService).update(questionId, "수정된 제목", "수정된 풀이");
		assertThat(result).isEqualTo("문제를 성공적으로 수정하였습니다.");
	}
}
