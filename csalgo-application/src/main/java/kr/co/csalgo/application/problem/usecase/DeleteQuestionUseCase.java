package kr.co.csalgo.application.problem.usecase;

import org.springframework.stereotype.Service;

import kr.co.csalgo.common.message.MessageCode;
import kr.co.csalgo.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteQuestionUseCase {
	private final QuestionService questionService;

	public String deleteQuestion(Long questionId) {
		questionService.delete(questionId);
		log.info("[문제 삭제] questionId:{} 삭제 완료", questionId);
		return MessageCode.DELETE_QUESTION_SUCCESS.getMessage();
	}
}
