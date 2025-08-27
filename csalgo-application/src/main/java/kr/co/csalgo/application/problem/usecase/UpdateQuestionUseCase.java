package kr.co.csalgo.application.problem.usecase;

import org.springframework.stereotype.Service;

import kr.co.csalgo.application.problem.dto.QuestionDto;
import kr.co.csalgo.common.message.CommonResponse;
import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateQuestionUseCase {
	private final QuestionService questionService;

	public CommonResponse updateQuestion(Long questionId, QuestionDto.Request request) {
		questionService.update(questionId, request.getTitle(), request.getSolution());
		return new CommonResponse(MessageCode.UPDATE_QUESTION_SUCCESS.getMessage());
	}
}
