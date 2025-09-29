package kr.co.csalgo.server.gpt;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GptController {

	private final FeedbackAnalyzer feedbackAnalyzer;

	@GetMapping("/gpt-test")
	public FeedbackResult testGpt() {
		String question = "시간 복잡도와 공간 복잡도를 설명하시오.";
		String solution = "시간 복잡도는 입력 크기와 연산 횟수의 관계를 나타내며, 공간 복잡도는 추가 메모리 사용량을 의미한다.";
		String userAnswer = "시간 복잡도는 연산 횟수, 공간 복잡도는 메모리 크기와 관련있습니다.";

		return feedbackAnalyzer.analyze(question, userAnswer, solution);
	}
}
