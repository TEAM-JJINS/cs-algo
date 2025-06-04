package kr.co.csalgo.presentation.question;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.csalgo.application.problem.dto.SendQuestionMailDto;
import kr.co.csalgo.application.problem.usecase.SendQuestionMailUseCase;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {
	private final SendQuestionMailUseCase sendQuestionMailUseCase;

	@PostMapping("/{questionId}/send")
	@Operation(summary = "문제 메일 전송", description = "사용자가 문제를 확인할 수 있도록 메일을 전송합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "메일 전송 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패 등)"),
		@ApiResponse(responseCode = "404", description = "문제 또는 사용자 정보가 존재하지 않음"),
	})
	public ResponseEntity<?> sendQuestionMail(
		@PathVariable Long questionId,
		@RequestParam(required = false) Long userId,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime) {
		SendQuestionMailDto.Request request = SendQuestionMailDto.Request.builder()
			.questionId(questionId)
			.userId(userId)
			.scheduledTime(scheduledTime)
			.build();

		return ResponseEntity.ok(sendQuestionMailUseCase.execute(request));
	}
}

