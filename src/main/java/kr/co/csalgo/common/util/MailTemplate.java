package kr.co.csalgo.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.core.io.ClassPathResource;

public class MailTemplate {
	public static final String QUESTION_MAIL_SUBJECT = "[CS-ALGO] %s";
	public static final String VERIFICATION_CODE_SUBJECT = "[CS-ALGO] 이메일 인증 코드";
	public static final String VERIFICATION_CODE_BODY = "<h3>인증 코드</h3><p>%s</p>";
	public static final String FEEDBACK_MAIL_SUBJECT_REPLY = "Re: [CS-ALGO] %s";

	public static String formatVerificationCodeBody(String code) {
		return VERIFICATION_CODE_BODY.formatted(code);
	}

	public static String formatQuestionMailBody(String questionTitle, Long index) {
		String logoBase64 = toBase64("static/images/logo.svg");
		String questionIcon = toBase64("static/images/question.svg");
		String usageIcon = toBase64("static/images/usage.svg");
		String unsubscriptionIcon = toBase64("static/images/unsubscription.svg");

		return """
			<!DOCTYPE html>
			<html lang="ko">
			<head>
				<meta charset="UTF-8">
				<meta name="color-scheme" content="only light">
				<style>
					html, body { color-scheme: only light; }
				</style>
			</head>
			<body style="margin:0; padding:0; background-color:#f6f6f6;" bgcolor="#f6f6f6">
				<table align="center" width="100%%" cellpadding="0" cellspacing="0" style="padding:20px 0;">
					<tr>
						<td align="center">
							<table width="600" cellpadding="0" cellspacing="0"
								style="background:#ffffff; border:1px solid #ddd;
								border-radius:12px; font-family:sans-serif; overflow:hidden;" bgcolor="#ffffff">

								<!-- 헤더 -->
								<tr>
									<td style="background:#1c1c1c; padding:16px 24px;" bgcolor="#1c1c1c">
										<img src="data:image/svg+xml;base64,%s" width="100" alt="CS-ALGO" style="display:block;">
									</td>
								</tr>

								<!-- 질문 번호 -->
								<tr>
									<td style="padding:24px 24px 0; font-size:14px; color:#333;">
										%d번째 질문이에요!
									</td>
								</tr>

								<!-- 질문 제목 -->
								<tr>
									<td style="padding:8px 24px 0;">
										<img src="data:image/svg+xml;base64,%s" width="20" style="vertical-align:middle; margin-right:6px;">
										<span style="font-size:20px; font-weight:bold;">%s</span>
									</td>
								</tr>

								<!-- 안내 문구 -->
								<tr>
									<td style="padding:24px 24px 0; font-size:15px; color:#333;">
										메일의 답장 버튼을 통해 메일을 전송하면, 답변에 대한 피드백을 드릴게요!
									</td>
								</tr>

								<!-- 버튼 영역 -->
								<tr>
									<td align="center" style="padding:32px 24px;">
										<table cellpadding="0" cellspacing="0">
											<tr>
												<td align="center" style="padding:0 20px;">
													<img src="data:image/svg+xml;base64,%s" width="24" style="display:block; margin-bottom:8px;">
													<div style="font-size:14px; color:#333;">이용방법</div>
												</td>
												<td align="center" style="padding:0 20px;">
													<img src="data:image/svg+xml;base64,%s" width="24" style="display:block; margin-bottom:8px;">
													<div style="font-size:14px; color:#333;">구독 해지</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>

								<!-- 푸터 -->
								<tr>
									<td align="center" style="font-size:12px; color:#aaa; padding-bottom:24px;">
										이 메일은 CS-ALGO에서 자동으로 전송되었습니다.
									</td>
								</tr>

							</table>
						</td>
					</tr>
				</table>
			</body>
			</html>
			""".formatted(
			logoBase64,
			index,
			questionIcon,
			escapeHtml(questionTitle),
			usageIcon,
			unsubscriptionIcon
		);
	}

	public static String formatFeedbackMailBody(String username,
		String questionTitle,
		String userAnswer,
		String modelAnswer) {

		String logoBase64 = toBase64("static/images/logo.svg");
		String questionIcon = toBase64("static/images/question.svg");
		String responseIcon = toBase64("static/images/response.svg");
		String solutionIcon = toBase64("static/images/solution.svg");

		return """
			<!DOCTYPE html>
			<html lang="ko">
			<body style="margin:0; padding:0; background-color:#f6f6f6;">
				<table align="center" width="100%%" cellpadding="0" cellspacing="0" style="padding:20px 0;">
					<tr>
						<td align="center">
							<table width="600"
								cellpadding="0" cellspacing="0"
								style="background:#ffffff; border:1px solid #ddd;
								border-radius:12px; font-family:sans-serif;
								overflow:hidden;">

								<!-- 헤더 -->
								<tr>
									<td style="background:#1c1c1c; padding:16px 24px;">
										<img src="data:image/svg+xml;base64,%s" width="100"
											alt="CS-ALGO" style="display:block;">
									</td>
								</tr>

								<!-- 질문 -->
								<tr>
									<td style="padding:24px 24px 0;">
										<img src="data:image/svg+xml;base64,%s" width="20"
											style="vertical-align:middle; margin-right:6px;">
										<span style="font-size:18px; font-weight:bold;">CS-ALGO가 질문한 내용이에요!</span>
									</td>
								</tr>
								<tr>
									<td style="padding:8px 24px 0; font-size:15px; color:#333; line-height:1.6;">
										%s
									</td>
								</tr>

								<!-- 사용자 답변 -->
								<tr>
									<td style="padding:32px 24px 0;">
										<img src="data:image/svg+xml;base64,%s" width="20"
											style="vertical-align:middle; margin-right:6px;">
										<span style="font-size:18px; font-weight:bold;">%s님이 이렇게 답변했어요!</span>
									</td>
								</tr>
								<tr>
									<td style="padding:8px 24px 0; font-size:15px; color:#333; line-height:1.6;">
										%s
									</td>
								</tr>

								<!-- 모델 피드백 -->
								<tr>
									<td style="padding:32px 24px 0;">
										<img src="data:image/svg+xml;base64,%s" width="20"
											style="vertical-align:middle; margin-right:6px;">
										<span style="font-size:18px; font-weight:bold;">이렇게 답변해보면 어떨까요? (by CS-ALGO)</span>
									</td>
								</tr>
								<tr>
									<td style="padding:8px 24px 36px; font-size:15px; color:#333; line-height:1.6;">
										%s
									</td>
								</tr>

								<!-- 푸터 -->
								<tr>
									<td align="center"
										style="font-size:12px; color:#aaa; padding-bottom:24px;">
										이 메일은 CS-ALGO에서 자동으로 전송되었습니다.
									</td>
								</tr>

							</table>
						</td>
					</tr>
				</table>
			</body>
			</html>
			""".formatted(
			logoBase64,
			questionIcon,
			escapeHtml(questionTitle),
			responseIcon,
			escapeHtml(username),
			escapeHtml(userAnswer),
			solutionIcon,
			escapeHtml(modelAnswer)
		);

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
