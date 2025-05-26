package kr.co.csalgo.common.exception;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
		String detail = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(ErrorCode.INVALID_INPUT, detail));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException exception) {
		String detail = exception.getMessage();
		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(ErrorCode.MESSGAE_NOT_READABLE, detail));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(ErrorCode.INVALID_INPUT, exception.getMessage()));
	}

	@ExceptionHandler(CustomBusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusiness(CustomBusinessException exception) {
		return ResponseEntity.status(exception.getErrorCode().getStatus())
			.body(ErrorResponse.of(exception.getErrorCode(), exception.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception exception) {
		return ResponseEntity.internalServerError()
			.body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage()));
	}
}
