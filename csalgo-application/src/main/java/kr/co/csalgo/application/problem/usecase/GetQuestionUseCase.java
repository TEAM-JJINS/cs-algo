package kr.co.csalgo.application.problem.usecase;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import kr.co.csalgo.application.problem.dto.QuestionDto;
import kr.co.csalgo.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetQuestionUseCase {
	private final QuestionService questionService;

	public Page<QuestionDto.Response> getQuestionListWithPaging(int page, int size) {
		return questionService.list(page, size)
			.map(QuestionDto.Response::of);
	}

}
