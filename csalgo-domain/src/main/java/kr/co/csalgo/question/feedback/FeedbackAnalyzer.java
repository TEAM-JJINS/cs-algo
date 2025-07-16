package kr.co.csalgo.question.feedback;

public interface FeedbackAnalyzer {
	FeedbackResult analyze(String responseContent, String questionSolution);
}
