package kr.co.csalgo.common.util;

public class MailTemplate {
	public static final String QUESTION_MAIL_SUBJECT = "[CS-ALGO] %s";
	public static final String VERIFICATION_CODE_SUBJECT = "[CS-ALGO] 이메일 인증 코드";
	public static final String VERIFICATION_CODE_BODY = "<h3>인증 코드</h3><p>%s</p>";

	public static String formatVerificationCodeBody(String code) {
		return VERIFICATION_CODE_BODY.formatted(code);
	}
}
