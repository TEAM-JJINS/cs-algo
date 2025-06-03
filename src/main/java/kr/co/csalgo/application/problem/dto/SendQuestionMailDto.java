package kr.co.csalgo.application.problem.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import kr.co.csalgo.common.message.MessageCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SendQuestionMailDto {
	@Getter
	@NoArgsConstructor
	public static class Request {
		@NotNull(message = "문제 ID는 필수입니다.")
		private Long questionId;

		private Long userId;

		@Future(message = "예약 발송 시간은 현재보다 미래여야 합니다.")
		private LocalDateTime scheduledTime;

		@Builder
		public Request(Long questionId, Long userId, LocalDateTime scheduledTime) {
			this.questionId = questionId;
			this.userId = userId;
			this.scheduledTime = scheduledTime;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Response {
		private String message;

		@Builder
		public Response(String message) {
			this.message = message;
		}

		public static Response of() {
			return Response.builder()
				.message(MessageCode.SEND_QUESTION_MAIL_SUCCESS.getMessage())
				.build();
		}
	}
}

