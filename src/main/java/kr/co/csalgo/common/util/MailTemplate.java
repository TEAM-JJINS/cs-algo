package kr.co.csalgo.common.util;

public class MailTemplate {
	public static final String QUESTION_MAIL_SUBJECT = "[CS-ALGO] %s";
	public static final String VERIFICATION_CODE_SUBJECT = "[CS-ALGO] 이메일 인증 코드";
	public static final String VERIFICATION_CODE_BODY = "<h3>인증 코드</h3><p>%s</p>";

	public static String formatVerificationCodeBody(String code) {
		return VERIFICATION_CODE_BODY.formatted(code);
	}

	public static String formatFeedbackMailBody(String username, String userAnswer, String modelAnswer) {
		return """
			<h2>%s님이 이렇게 말했어요!</h2>
			<blockquote>%s</blockquote>

			<h2>이런 식으로 답변해보는 건 어떨까요? (by CS-ALGO)</h2>
			<blockquote>%s</blockquote>
			""".formatted(username, escapeHtml(userAnswer), escapeHtml(modelAnswer));
	}

	private static String escapeHtml(String input) {
		if (input == null) {
			return "";
		}

		return input.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;");
	}
}
