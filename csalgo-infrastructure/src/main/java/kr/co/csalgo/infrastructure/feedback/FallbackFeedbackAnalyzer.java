package kr.co.csalgo.infrastructure.feedback;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import kr.co.csalgo.domain.question.feedback.FeedbackAnalyzer;
import kr.co.csalgo.domain.question.feedback.FeedbackResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class FallbackFeedbackAnalyzer implements FeedbackAnalyzer {

	private final GptFeedbackAnalyzer gptAnalyzer;
	private final SimpleFeedbackAnalyzer simpleAnalyzer;

	@Override
	public FeedbackResult analyze(String questionTitle, String responseContent, String questionSolution) {
		try {
			return gptAnalyzer.analyze(questionTitle, responseContent, questionSolution);
		} catch (Exception e) {
			log.warn("[GPT 분석 실패] Simple 분석으로 대체, reason={}", e.getMessage());
			return simpleAnalyzer.analyze(questionTitle, responseContent, questionSolution);
		}
	}
}

