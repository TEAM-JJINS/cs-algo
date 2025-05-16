package kr.co.csalgo.common.exception;

public enum ErrorCode {

    DUPLICATE_EMAIL("이메일이 이미 등록되어 있습니다.");


    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
