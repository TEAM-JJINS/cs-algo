package kr.co.csalgo.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;

public class MailTemplate {
	public static final String QUESTION_MAIL_SUBJECT = "[CS-ALGO] %s";
	public static final String VERIFICATION_CODE_SUBJECT = "[CS-ALGO] 이메일 인증 코드";
	public static final String VERIFICATION_CODE_BODY = "<h3>인증 코드</h3><p>%s</p>";
	public static final String FEEDBACK_MAIL_SUBJECT_REPLY = "Re: [CS-ALGO] %s";

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
				<body style="margin:0; padding:0; background-color:#f6f6f6;" bgcolor="#f6f6f6">
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
			""".formatted(bodyContent);
	}

	private static String headerSection() {
		String logoBase64 = toBase64("static/images/logo.svg");
		return """
				<tr><td style="background:#1c1c1c; padding:20px 24px;" bgcolor="#1c1c1c">
					<a href="http://www.csalgo.co.kr" target="_blank">
						<img src="data:image/svg+xml;base64,%s" width="100" alt="CS-ALGO" style="display:block;">
					</a>
				</td></tr>
			""".formatted(logoBase64);
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
		String questionIcon = toBase64("static/images/question.svg");
		return """
				<tr><td style="padding:0 24px;">
					<img src="data:image/svg+xml;base64,%s" width="20" style="vertical-align:middle; margin-right:6px;">
					<span style="font-size:22px; font-weight:bold;">%s</span>
				</td></tr>
			""".formatted(questionIcon, escapeHtml(questionTitle));
	}

	private static String instructionSection() {
		return """
				<tr><td style="padding:32px 24px 0; font-size:15px; color:#333;">
					메일의 답장 버튼을 통해 메일을 전송하면, 답변에 대한 피드백을 드릴게요!
				</td></tr>
			""";
	}

	private static String buttonSection(UUID userId) {
		String usageIcon = toBase64("static/images/usage.svg");
		String unsubscribeIcon = toBase64("static/images/unsubscription.svg");
		return """
				<tr><td align="center" style="padding:36px 24px;">
					<table cellpadding="0" cellspacing="0">
						<tr>
							<td align="center" style="padding:0 28px;">
								<a href="#" target="_blank" style="text-decoration:none;">
									<img src="data:image/svg+xml;base64,%s" width="24" style="display:block; margin-bottom:10px;">
									<div style="font-size:14px; color:#333;">이용방법</div>
								</a>
							</td>
							<td align="center" style="padding:0 28px;">
								<a href="http://www.csalgo.co.kr/unsubscription?userId=%s" target="_blank" style="text-decoration:none;">
									<img src="data:image/svg+xml;base64,%s" width="24" style="display:block; margin-bottom:10px;">
									<div style="font-size:14px; color:#333;">구독 해지</div>
								</a>
							</td>
						</tr>
					</table>
				</td></tr>
			""".formatted(usageIcon, userId.toString(), unsubscribeIcon);
	}

	private static String footerSection() {
		return """
				<tr><td align="center" style="font-size:12px; color:#aaa; padding-bottom:28px;">
					이 메일은 CS-ALGO에서 자동으로 전송되었습니다.
				</td></tr>
			""";
	}

	private static String feedbackQuestionSection(String questionTitle) {
		String questionIcon = toBase64("static/images/question.svg");
		return """
				<tr><td style="padding:24px 24px 0;">
					<img src="data:image/svg+xml;base64,%s" width="20" style="vertical-align:middle; margin-right:6px;">
					<span style="font-size:18px; font-weight:bold;">CS-ALGO가 질문한 내용이에요!</span>
				</td></tr>
				<tr><td style="padding:8px 24px 0; font-size:15px; color:#333; line-height:1.6;">
					%s
				</td></tr>
			""".formatted(questionIcon, escapeHtml(questionTitle));
	}

	private static String feedbackUserAnswerSection(String username, String userAnswer) {
		String responseIcon = toBase64("static/images/response.svg");
		return """
				<tr><td style="padding:32px 24px 0;">
					<img src="data:image/svg+xml;base64,%s" width="20" style="vertical-align:middle; margin-right:6px;">
					<span style="font-size:18px; font-weight:bold;">%s님이 이렇게 답변했어요!</span>
				</td></tr>
				<tr><td style="padding:8px 24px 0; font-size:15px; color:#333; line-height:1.6;">
					%s
				</td></tr>
			""".formatted(responseIcon, escapeHtml(username), escapeHtml(userAnswer));
	}

	private static String feedbackModelAnswerSection(String modelAnswer) {
		String solutionIcon = toBase64("static/images/solution.svg");
		return """
				<tr><td style="padding:32px 24px 0;">
					<img src="data:image/svg+xml;base64,%s" width="20" style="vertical-align:middle; margin-right:6px;">
					<span style="font-size:18px; font-weight:bold;">이렇게 답변해보면 어떨까요? (by CS-ALGO)</span>
				</td></tr>
				<tr><td style="padding:8px 24px 36px; font-size:15px; color:#333; line-height:1.6;">
					%s
				</td></tr>
			""".formatted(solutionIcon, escapeHtml(modelAnswer));
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

	private static String toBase64(String pathInClassPath) {
		try (InputStream input = new ClassPathResource(pathInClassPath).getInputStream()) {
			byte[] bytes = input.readAllBytes();
			return Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			throw new IllegalStateException("이미지 인코딩 실패: " + pathInClassPath, e);
		}
	}
}
