package kr.co.csalgo.common.exception;

import lombok.Getter;

@Getter
public class ApiClientException extends RuntimeException {
	private final ErrorResponse errorResponse;

	public ApiClientException(ErrorResponse errorResponse) {
		super(errorResponse.getMessage());
		this.errorResponse = errorResponse;
	}
}
