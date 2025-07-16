package kr.co.csalgo.similarity;

import lombok.Getter;

@Getter
public enum SimilarityGuide {
	EXCELLENT(90, 100, "🎉 완벽해요! 자신감을 가지세요 🙌"),
	GREAT(70, 89, "👍 훌륭해요! 거의 다 왔어요. 조금만 더 다듬어보세요."),
	GOOD(50, 69, "🌱 좋아요! 핵심 키워드를 조금 더 넣어보면 더 좋아질 거예요."),
	FAIR(30, 49, "💡 괜찮아요! 핵심 내용을 조금 더 보완해보세요."),
	POOR(0, 29, "🚀 아직 연습이 필요해요. 모범 답안을 참고해 더 작성해보세요.");

	private final int min;
	private final int max;
	private final String message;

	SimilarityGuide(int min, int max, String message) {
		this.min = min;
		this.max = max;
		this.message = message;
	}

	public static String getGuideMessage(double score) {
		int percent = (int)Math.round(score);
		for (SimilarityGuide guide : values()) {
			if (percent >= guide.min && percent <= guide.max) {
				return guide.message;
			}
		}
		return POOR.message;
	}
}

