package kr.co.csalgo.common.exception;

import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
		logException(exception, "WARN");
		String message = Optional.ofNullable(exception.getBindingResult().getFieldError())
			.map(FieldError::getDefaultMessage)
			.orElse("잘못된 요청입니다.");
		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(ErrorCode.INVALID_INPUT, message));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException exception) {
		logException(exception, "WARN");
		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(ErrorCode.MESSGAE_NOT_READABLE, exception.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
		logException(exception, "WARN");
		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(ErrorCode.INVALID_INPUT, exception.getMessage()));
	}

	@ExceptionHandler(CustomBusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusiness(CustomBusinessException exception) {
		logException(exception, "INFO");
		return ResponseEntity.status(exception.getErrorCode().getStatus())
			.body(ErrorResponse.of(exception.getErrorCode(), exception.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception exception) {
		logException(exception, "ERROR");
		return ResponseEntity.internalServerError()
			.body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage()));
	}

	private void logException(Exception ex, String logLevel) {
		if (ex instanceof MethodArgumentNotValidException e) {
			MethodParameter param = e.getParameter();
			FieldError fieldError = e.getBindingResult().getFieldError();

			String controller = param.getContainingClass().getSimpleName();
			String method = param.getMethod().getName();
			String field = fieldError.getField();
			String message = fieldError.getDefaultMessage();

			String logMessage = String.format(
				"Validation failed | controller=%s | method=%s | field=%s | message=%s",
				controller, method, field, message
			);
			log(logLevel, logMessage);
		} else {
			String location = ex.getStackTrace().length > 0
				? ex.getStackTrace()[0].toString()
				: "UnknownLocation";
			log(logLevel, "Exception occurred at {}: {}", location, ex.getMessage());
		}
	}

	private void log(String level, String format, Object... args) {
		switch (level) {
			case "ERROR" -> log.error(format, args);
			case "WARN" -> log.warn(format, args);
			case "INFO" -> log.info(format, args);
			case "DEBUG" -> log.debug(format, args);
			default -> log.info(format, args);
		}
	}
}
