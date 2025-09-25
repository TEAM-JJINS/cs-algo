package kr.co.csalgo.domain.question.feedback;

public interface FeedbackAnalyzer {
	FeedbackResult analyze(String questionTitle, String responseContent, String questionSolution);
}
