package kr.co.csalgo.domain.ai;

import kr.co.csalgo.domain.question.feedback.FeedbackResult;

public interface AiClient {
	FeedbackResult ask(String question, String solution, String userAnswer, double similarityScore);
}
