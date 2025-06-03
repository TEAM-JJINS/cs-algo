package kr.co.csalgo.presentation.question;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.application.problem.usecase.SendQuestionMailUseCase;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
	private final SendQuestionMailUseCase sendQuestionMailUseCase;

	@PostMapping("/send")
	@Operation(summary = "문제 메일 전송", description = "사용자가 문제를 확인할 수 있도록 메일을 전송합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "메일 전송 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패 등)"),
		@ApiResponse(responseCode = "404", description = "문제 또는 사용자 정보가 존재하지 않음"),
	})
	public ResponseEntity<?> sendQuestionMail(@Valid @RequestBody SendQuestionMailDto.Request request) {
		return ResponseEntity.ok(sendQuestionMailUseCase.execute(request));
	}
}

