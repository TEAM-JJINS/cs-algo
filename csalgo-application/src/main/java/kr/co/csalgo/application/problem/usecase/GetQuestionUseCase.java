package kr.co.csalgo.application.problem.usecase;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.co.csalgo.application.common.dto.PagedResponse;
import kr.co.csalgo.application.problem.dto.QuestionDto;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetQuestionUseCase {
	private final QuestionService questionService;

	public PagedResponse<QuestionDto.Response> getQuestionListWithPaging(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Question> questionPage = questionService.list(pageable);

		List<QuestionDto.Response> questions = questionPage.getContent().stream()
			.map(QuestionDto.Response::of)
			.toList();

		log.info("[문제 리스트 조회 완료] count:{}, page:{}/{}",
			questions.size(), page, questionPage.getTotalPages());

		return PagedResponse.<QuestionDto.Response>builder()
			.content(questions)
			.currentPage(page)
			.totalPages(questionPage.getTotalPages())
			.totalElements(questionPage.getTotalElements())
			.first(questionPage.isFirst())
			.last(questionPage.isLast())
			.build();
	}

	public QuestionDto.Response getQuestionDetail(Long questionId) {
		return QuestionDto.Response.of(questionService.read(questionId));
	}
}
