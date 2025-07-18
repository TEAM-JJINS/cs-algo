package kr.co.csalgo.application.problem.usecase;

import java.util.List;

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

	public List<QuestionDto.Response> getQuestionList() {
		return questionService.list().stream()
			.map(QuestionDto.Response::of)
			.toList();
	}

}
