package kr.co.csalgo.common.util;

import java.util.Objects;
import java.util.UUID;

public class MailTemplate {

	public static final String QUESTION_MAIL_SUBJECT = "[CS-ALGO] %s";
	public static final String VERIFICATION_CODE_SUBJECT = "[CS-ALGO] 이메일 인증 코드";
	public static final String FEEDBACK_MAIL_SUBJECT_REPLY = "Re: [CS-ALGO] %s";

	private static final String LOGO_URL = "https://csalgo-bucket.s3.ap-northeast-2.amazonaws.com/image/logo.png";
	private static final String QUESTION_ICON_URL = "https://csalgo-bucket.s3.ap-northeast-2.amazonaws.com/image/question.png";
	private static final String RESPONSE_ICON_URL = "https://csalgo-bucket.s3.ap-northeast-2.amazonaws.com/image/response.png";
	private static final String SOLUTION_ICON_URL = "https://csalgo-bucket.s3.ap-northeast-2.amazonaws.com/image/solution.png";
	private static final String USAGE_ICON_URL = "https://csalgo-bucket.s3.ap-northeast-2.amazonaws.com/image/usage.png";
	private static final String UNSUBSCRIPTION_ICON_URL = "https://csalgo-bucket.s3.ap-northeast-2.amazonaws.com/image/cancel.png";

	private static final String PRIMARY_COLOR = "#1c1c1c";
	private static final String BACKGROUND_COLOR = "#f6f6f6";

	public static String formatVerificationCodeBody(String code) {
		return wrapHtml(
			headerSection()
				+ verificationCodeSection(code)
				+ footerSection()
		);
	}

	public static String formatQuestionMailBody(String questionTitle, Long index, UUID userId) {
		return wrapHtml(
			headerSection()
				+ questionIndexSection(index)
				+ questionTitleSection(questionTitle)
				+ instructionSection()
				+ buttonSection(userId)
				+ footerSection()
		);
	}

	public static String formatFeedbackMailBody(String username, String questionTitle, String userAnswer, String modelAnswer) {
		return wrapHtml(
			headerSection()
				+ feedbackQuestionSection(questionTitle)
				+ feedbackUserAnswerSection(username, userAnswer)
				+ feedbackModelAnswerSection(modelAnswer)
				+ footerSection()
		);
	}

	private static String wrapHtml(String bodyContent) {
		return """
			<!DOCTYPE html>
			<html lang="ko">
			<head>
				<meta charset="UTF-8">
				<meta name="color-scheme" content="only light">
				<style>
					html, body { color-scheme: only light; }
					a { text-decoration: none; color: inherit; }
				</style>
			</head>
			<body style="margin:0; padding:0; background-color:%s;" bgcolor="%s">
				<table align="center" width="100%%" cellpadding="0" cellspacing="0" style="padding:24px 0;">
					<tr>
						<td align="center">
							<table width="600" cellpadding="0" cellspacing="0"
								style="background:#ffffff; border:1px solid #ddd; border-radius:18px;
								font-family:sans-serif; overflow:hidden;" bgcolor="#ffffff">
								%s
							</table>
						</td>
					</tr>
				</table>
			</body>
			</html>
			""".formatted(BACKGROUND_COLOR, BACKGROUND_COLOR, bodyContent);
	}

	private static String headerSection() {
		return """
			<tr><td style="background:%s; padding:20px 24px;" bgcolor="%s">
				<a href="https://www.csalgo.co.kr" target="_blank">
					<img src="%s" width="100" alt="CS-ALGO" style="display:block;">
				</a>
			</td></tr>
			""".formatted(PRIMARY_COLOR, PRIMARY_COLOR, LOGO_URL);
	}

	private static String verificationCodeSection(String code) {
		return """
			<tr>
				<td align="center" style="padding:48px 24px 0; font-size:16px; color:#333;">
					이메일 인증을 위해 아래 코드를 입력해주세요.
				</td>
			</tr>
			<tr>
				<td align="center" style="padding:36px 24px 64px;">
					<div style="font-size:48px; font-weight:bold; letter-spacing:8px; color:#000;">
						%s
					</div>
				</td>
			</tr>
			""".formatted(escapeHtml(code));
	}

	private static String questionIndexSection(Long index) {
		return """
			<tr><td style="padding:32px 24px 8px; font-size:15px; color:#333;">
				%d번째 질문이에요!
			</td></tr>
			""".formatted(index);
	}

	private static String questionTitleSection(String questionTitle) {
		return """
			<tr><td style="padding:0 24px;">
				<img src="%s" width="20" style="vertical-align:middle; margin-right:6px; border:2px solid #ffffff; border-radius:4px;">
				<span style="font-size:22px; font-weight:bold;">%s</span>
			</td></tr>
			""".formatted(QUESTION_ICON_URL, escapeHtml(questionTitle));
	}

	private static String instructionSection() {
		return """
			<tr><td style="padding:32px 24px 0; font-size:15px; color:#333;">
				메일의 답장 버튼을 통해 메일을 전송하면, 답변에 대한 피드백을 드릴게요!
			</td></tr>
			""";
	}

	private static String buttonSection(UUID userId) {
		return """
			<tr><td align="center" style="padding:36px 24px;">
				<table cellpadding="0" cellspacing="0">
					<tr>
						<td align="center" style="padding:0 28px;">
							<a href="https://csalgo-bucket.s3.ap-northeast-2.amazonaws.com/usage/usage.html" target="_blank" style="text-decoration:none;">
								<img src="%s" width="24" style="display:block; margin-bottom:10px; border:2px solid #ffffff; border-radius:4px;">
								<div style="font-size:14px; color:#333;">이용방법</div>
							</a>
						</td>
						<td align="center" style="padding:0 28px;">
							<a href="https://www.csalgo.co.kr/unsubscription?userId=%s" target="_blank" style="text-decoration:none;">
								<img src="%s" width="24" style="display:block; margin-bottom:10px; border:2px solid #ffffff; border-radius:4px;">
								<div style="font-size:14px; color:#333;">구독 해지</div>
							</a>
						</td>
					</tr>
				</table>
			</td></tr>
			""".formatted(USAGE_ICON_URL, userId, UNSUBSCRIPTION_ICON_URL);
	}

	private static String footerSection() {
		return """
			<tr><td align="center" style="font-size:12px; color:#aaa; padding-bottom:28px;">
				이 메일은 CS-ALGO에서 자동으로 전송되었습니다.
			</td></tr>
			""";
	}

	private static String feedbackQuestionSection(String questionTitle) {
		return """
			<tr><td style="padding:24px 24px 0;">
				<img src="%s" width="20" style="vertical-align:middle; margin-right:6px; border:2px solid #ffffff; border-radius:4px;">
				<span style="font-size:18px; font-weight:bold;">CS-ALGO가 질문한 내용이에요!</span>
			</td></tr>
			<tr><td style="padding:8px 24px 0; font-size:15px; color:#333; line-height:1.6;">
				%s
			</td></tr>
			""".formatted(QUESTION_ICON_URL, escapeHtml(questionTitle));
	}

	private static String feedbackUserAnswerSection(String username, String userAnswer) {
		return """
			<tr><td style="padding:32px 24px 0;">
				<img src="%s" width="20" style="vertical-align:middle; margin-right:6px; border:2px solid #ffffff; border-radius:4px;">
				<span style="font-size:18px; font-weight:bold;">%s님이 이렇게 답변했어요!</span>
			</td></tr>
			<tr><td style="padding:8px 24px 0; font-size:15px; color:#333; line-height:1.6;">
				%s
			</td></tr>
			""".formatted(RESPONSE_ICON_URL, escapeHtml(username), escapeHtml(userAnswer));
	}

	private static String feedbackModelAnswerSection(String modelAnswer) {
		return """
			<tr><td style="padding:32px 24px 0;">
				<img src="%s" width="20" style="vertical-align:middle; margin-right:6px; border:2px solid #ffffff; border-radius:4px;">
				<span style="font-size:18px; font-weight:bold;">이렇게 답변해보면 어떨까요? (by CS-ALGO)</span>
			</td></tr>
			<tr><td style="padding:8px 24px 36px; font-size:15px; color:#333; line-height:1.6;">
				%s
			</td></tr>
			""".formatted(SOLUTION_ICON_URL, escapeHtml(modelAnswer));
	}

	private static String escapeHtml(String input) {
		return Objects.toString(input, "")
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;");
	}
}
