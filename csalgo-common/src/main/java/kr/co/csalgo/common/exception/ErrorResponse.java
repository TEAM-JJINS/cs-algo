package kr.co.csalgo.common.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorResponse {
	private String code;
	private String message;
	private String detail;
	private LocalDateTime timestamp;

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.getCode(),
			errorCode.getMessage(),
			null,
			LocalDateTime.now()
		);
	}

	public static ErrorResponse of(ErrorCode errorCode, String detail) {
		return new ErrorResponse(
			errorCode.getCode(),
			errorCode.getMessage(),
			detail,
			LocalDateTime.now()
		);
	}
}
