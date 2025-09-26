package kr.co.csalgo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	// A: 일반 요청 오류
	INVALID_INPUT("A001", HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
	MESSAGE_NOT_READABLE("A002", HttpStatus.BAD_REQUEST, "요청 본문을 읽을 수 없습니다.."),
	FILE_NOT_FOUND("A003", HttpStatus.BAD_REQUEST, "파일을 찾을 수 없습니다."),

	// B: 비즈니스 로직 오류
	USER_NOT_FOUND("B001", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	DUPLICATE_EMAIL("B002", HttpStatus.CONFLICT, "이메일이 이미 등록되어 있습니다."),
	VERIFICATION_CODE_MISMATCH("B003", HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
	CREDENTIAL_NOT_FOUND("B004", HttpStatus.NOT_FOUND, "인증 정보를 찾을 수 없습니다."),
	FORBIDDEN_ACCESS("B005", HttpStatus.FORBIDDEN, "접근이 금지되었습니다."),
	INVALID_TOKEN_TYPE("B006", HttpStatus.BAD_REQUEST, "유효하지 않은 토큰 타입입니다."),
	REFRESH_FAMILY_REVOKED("B007", HttpStatus.BAD_REQUEST, "리프레시 토큰 패밀리가 취소되었습니다."),
	REFRESH_TOKEN_REUSE("B008", HttpStatus.BAD_REQUEST, "리프레시 토큰 재사용이 감지되었습니다."),
	UNAUTHORIZED_ACCESS("B009", HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),
	ALREADY_ASSIGNED_ROLE("B010", HttpStatus.CONFLICT, "이미 해당 권한이 부여되어 있습니다."),

	// C: 문제 관련 오류
	QUESTION_NOT_FOUND("C001", HttpStatus.NOT_FOUND, "문제를 찾을 수 없습니다."),

	// Z: 시스템 오류
	INTERNAL_SERVER_ERROR("Z001", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
	EMAIL_RECEIVER_ERROR("Z002", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 수신 중 오류가 발생했습니다."),
	EMAIL_SENDER_ERROR("Z003", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 중 오류가 발생했습니다."),
	EMAIL_CONTENT_NOT_FOUND("Z004", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 본문을 찾을 수 없습니다."),
	EMAIL_CONTENT_PARSE_FAIL("Z005", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 본문 파싱에 실패했습니다."),
	EMAIL_SENDER_PARSE_FAIL("Z006", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 답변 송신자 메일 주소 파싱에 실패했습니다."),
	EMAIL_SUBJECT_PARSE_FAIL("Z007", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 제목 파싱에 실패했습니다."),
	SIMILARITY_CALCULATION_ERROR("Z008", HttpStatus.INTERNAL_SERVER_ERROR, "유사도 계산 중 오류가 발생했습니다."),
	BATCH_RETRY_ERROR("Z009", HttpStatus.INTERNAL_SERVER_ERROR, "재시도 실패"),
	LLM_RESPONSE_PARSE_FAIL("Z009", HttpStatus.INTERNAL_SERVER_ERROR, "LLM 응답 파싱에 실패했습니다");

	private final String code;
	private final HttpStatusCode status;
	private final String message;
}
