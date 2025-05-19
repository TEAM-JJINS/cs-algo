package kr.co.csalgo.application.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class UnsubscriptionUseCaseDto {
    @Getter
    public static class Request {
        @NotNull(message = "사용자 ID는 필수입니다.")
        private final Long userId;

        @Builder
        public Request(Long userId) {
            this.userId = userId;
        }
    }

    @Getter
    public static class Response {
        private final String message;

        @Builder
        public Response(String message) {
            this.message = message;
        }

        public static Response of() {
            return Response.builder()
                    .message("구독이 성공적으로 해지되었습니다.")
                    .build();
        }
    }
}
