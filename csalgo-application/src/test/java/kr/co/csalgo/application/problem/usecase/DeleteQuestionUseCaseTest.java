package kr.co.csalgo.application.problem.usecase;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.question.service.QuestionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteQuestionUseCase 테스트")
public class DeleteQuestionUseCaseTest {
	@Mock
	private QuestionService questionService;

	@InjectMocks
	private DeleteQuestionUseCase deleteQuestionUseCase;

	@Test
	@DisplayName("문제 삭제 성공 시 성공 메시지를 반환한다")
	void testDeleteQuestionSuccess() {
		Long questionId = 1L;

		String result = deleteQuestionUseCase.deleteQuestion(questionId).getMessage();

		verify(questionService, times(1)).delete(questionId);
		assertThat(result).isEqualTo(MessageCode.DELETE_QUESTION_SUCCESS.getMessage());
	}

}
