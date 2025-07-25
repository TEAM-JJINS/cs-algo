package kr.co.csalgo.server.question;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kr.co.csalgo.application.problem.dto.QuestionDto;
import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.application.problem.usecase.DeleteQuestionUseCase;
import kr.co.csalgo.application.problem.usecase.GetQuestionUseCase;
import kr.co.csalgo.application.problem.usecase.SendQuestionMailUseCase;
import kr.co.csalgo.application.problem.usecase.UpdateQuestionUseCase;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {
	private final GetQuestionUseCase getQuestionUseCase;
	private final SendQuestionMailUseCase sendQuestionMailUseCase;
	private final UpdateQuestionUseCase updateQuestionUseCase;
	private final DeleteQuestionUseCase deleteQuestionUseCase;

	@PostMapping("/{questionId}/send")
	@Operation(summary = "문제 메일 전송", description = "사용자가 문제를 확인할 수 있도록 메일을 전송합니다.")
	@ApiResponse(responseCode = "200", description = "메일 전송 성공")
	@ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패 등)")
	@ApiResponse(responseCode = "404", description = "문제 또는 사용자 정보가 존재하지 않음")
	@SuppressWarnings("java:S1452")
	public ResponseEntity<?> sendQuestionMail(
		@PathVariable Long questionId,
		@RequestBody @Valid SendQuestionMailDto.Request request) {
		request.setQuestionId(questionId);

		return ResponseEntity.ok(sendQuestionMailUseCase.execute(request));
	}

	@GetMapping("")
	@Operation(summary = "문제 목록 조회", description = "관리자는 문제 목록을 조회할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "문제 목록 조회 성공")
	public ResponseEntity<?> getQuestionList(
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(getQuestionUseCase.getQuestionListWithPaging(page, size));
	}

	@GetMapping("/{questionId}")
	@Operation(summary = "문제 상세 조회", description = "관리자는 문제 상세정보를 조회할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "문제 상세 조회 성공")
	public ResponseEntity<?> getQuestionDetail(@PathVariable Long questionId) {
		return ResponseEntity.ok(getQuestionUseCase.getQuestionDetail(questionId));
	}

	@PutMapping("/{questionId}")
	@Operation(summary = "문제 수정", description = "관리자는 문제를 수정할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "문제 수정 성공")
	public ResponseEntity<?> questionUpdate(@PathVariable Long questionId, @RequestBody QuestionDto.Request request) {
		String message = updateQuestionUseCase.updateQuestion(questionId, request);
		return ResponseEntity.status(HttpStatus.OK).body(message);
	}

	@DeleteMapping("/{questionId}")
	@Operation(summary = "문제 삭제", description = "관리자는 문제를 삭제할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "문제 삭제 성공")
	public ResponseEntity<?> questionDelete(@PathVariable Long questionId) {
		return ResponseEntity.ok(deleteQuestionUseCase.deleteQuestion(questionId));
	}
}

