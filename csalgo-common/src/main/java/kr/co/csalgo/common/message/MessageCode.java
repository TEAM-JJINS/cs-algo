package kr.co.csalgo.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCode {
	//구독 관련
	SUBSCRIBE_SUCCESS("구독이 성공적으로 완료되었습니다."),
	UNSUBSCRIBE_SUCCESS("구독이 성공적으로 해지되었습니다."),

	// 인증 관련
	EMAIL_SENT_SUCCESS("인증 메일이 성공적으로 전송되었습니다."),
	EMAIL_VERIFICATION_SUCCESS("이메일 인증이 성공적으로 완료되었습니다."),

	// 문제 관련
	SEND_QUESTION_MAIL_SUCCESS("문제 메일이 성공적으로 발송(예약)되었습니다."),
	UPDATE_QUESTION_SUCCESS("문제를 성공적으로 수정하였습니다."),
	DELETE_QUESTION_SUCCESS("문제를 성공적으로 삭제했습니다.");

	private final String message;
}
