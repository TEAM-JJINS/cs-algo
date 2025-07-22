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

	public List<QuestionDto.Response> getQuestionListWithPaging(int page, int size) {
		List<QuestionDto.Response> questions = questionService.list(page - 1, size).stream()
			.map(QuestionDto.Response::of)
			.toList();
		log.info("[문제 리스트 조회 완료] count:{}", questions.size());
		return questions;
	}

}
