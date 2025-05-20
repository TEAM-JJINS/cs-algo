package kr.co.csalgo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // A: 일반 요청 오류
    INVALID_INPUT("A001", "잘못된 입력값입니다."),
    MESSGAE_NOT_READABLE("A002", "요청 본문을 읽을 수 없습니다.."),

    // B: 비즈니스 로직 오류
    USER_NOT_FOUND("B001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL("B002", "이메일이 이미 등록되어 있습니다."),

    // Z: 시스템 오류
    INTERNAL_SERVER_ERROR("Z001", "서버 내부가 발생했습니다.");

    private final String code;
    private final String message;
}
