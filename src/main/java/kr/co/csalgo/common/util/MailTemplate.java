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

	public static String formatFeedbackMailBody(String username, String userAnswer, String modelAnswer) {
		String logoBase64 = toBase64("static/images/logo.svg");
		String responseBase64 = toBase64("static/images/response.svg");
		String solutionBase64 = toBase64("static/images/solution.svg");

		return """
			<!DOCTYPE html>
			<html lang="ko">
			<body style="margin:0; padding:0; background-color:#f6f6f6;">
				<table align="center" width="100%%" cellpadding="0" cellspacing="0" style="padding: 20px 0;">
					<tr>
						<td align="center">
							<table width="600" style="background: #ffffff; border: 1px solid #ddd; font-family: sans-serif; overflow: hidden;">

								<!-- 카드 내부 헤더 -->
								<tr>
									<td style="background-color: #1c1c1c; padding: 16px 24px;">
										<img src="data:image/svg+xml;base64,%s" width="100" alt="CS-ALGO" style="display: block;" />
									</td>
								</tr>

								<tr><td style="height: 24px;"></td></tr>

								<!-- 사용자 응답 -->
								<tr>
									<td style="padding: 0 24px;">
										<img src="data:image/svg+xml;base64,%s" width="20" style="vertical-align: middle; margin-right: 6px;" />
										<span style="font-size: 18px; font-weight: bold;">%s님이 이렇게 답변했어요!</span>
									</td>
								</tr>
								<tr>
									<td style="padding: 8px 24px 0; font-size: 15px; color: #333; line-height: 1.6;">
										%s
									</td>
								</tr>

								<tr><td style="height: 32px;"></td></tr>

								<!-- 모델 피드백 -->
								<tr>
									<td style="padding: 0 24px;">
										<img src="data:image/svg+xml;base64,%s" width="20" style="vertical-align: middle; margin-right: 6px;" />
										<span style="font-size: 18px; font-weight: bold;">이렇게 답변해보면 어떨까요? (by CS-ALGO)</span>
									</td>
								</tr>
								<tr>
									<td style="padding: 8px 24px 0; font-size: 15px; color: #333; line-height: 1.6;">
										%s
									</td>
								</tr>

								<tr><td style="height: 36px;"></td></tr>

								<!-- 하단 문구 -->
								<tr>
									<td align="center" style="font-size: 12px; color: #aaa; padding-bottom: 24px;">
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
			responseBase64,
			escapeHtml(username),
			escapeHtml(userAnswer),
			solutionBase64,
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
