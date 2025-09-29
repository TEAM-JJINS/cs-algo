package kr.co.csalgo.common.util;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MailTemplateTest {

	@Test
	@DisplayName("이메일 인증 코드 메일 HTML 생성")
	void formatVerificationCodeBody() {
		String code = "123456";
		String html = MailTemplate.formatVerificationCodeBody(code);

		assertThat(html).contains("이메일 인증을 위해 아래 코드를 입력해주세요.");
		assertThat(html).contains(code);
		assertThat(html).contains("<html");
		assertThat(html).contains("</html>");
	}

	@Test
	@DisplayName("질문 메일 HTML 생성")
	void formatQuestionMailBody() {
		String questionTitle = "트랜잭션 격리 레벨을 설명하라.";
		long index = 42;
		UUID userId = UUID.randomUUID();

		String html = MailTemplate.formatQuestionMailBody(questionTitle, index, userId);

		assertThat(html).contains(index + "번째 질문이에요!");
		assertThat(html).contains(questionTitle);
		assertThat(html).contains("메일의 답장 버튼을 통해 메일을 전송하면");
		assertThat(html).contains("구독 해지");
	}

	@Test
	@DisplayName("피드백 메일 HTML 생성")
	void formatFeedbackMailBody() {
		String username = "진우";
		String questionTitle = "OSI 7계층을 설명하라.";
		String userAnswer = "네트워크를 7단계로 나눈 모델입니다.";
		String modelAnswer = "OSI 7계층은 물리, 데이터링크, 네트워크, 전송, 세션, 표현, 응용 계층으로 구분됩니다.";
		double similarity = 83.7;
		String guideMessage = "조금 더 구체적으로 답변해보세요!";

		String html = MailTemplate.formatFeedbackMailBody(
			username,
			questionTitle,
			userAnswer,
			modelAnswer,
			similarity,
			guideMessage,
			List.of(),   // strengths
			List.of(),   // improvements
			List.of()    // learningTips
		);

		assertThat(html).contains("CS-ALGO가 질문한 내용이에요!");
		assertThat(html).contains(questionTitle);
		assertThat(html).contains(username + "님이 이렇게 답변했어요!");
		assertThat(html).contains(userAnswer);
		assertThat(html).contains(String.format("📊 유사도: %.1f%%", similarity));
		assertThat(html).contains(guideMessage);
		assertThat(html).contains("이렇게 답변해보면 어떨까요?");
		assertThat(html).contains(modelAnswer);
	}

}
