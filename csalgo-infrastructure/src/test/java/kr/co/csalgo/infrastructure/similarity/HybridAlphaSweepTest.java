package kr.co.csalgo.infrastructure.similarity;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.csalgo.domain.similarity.SimilarityCalculator;

@Disabled
class HybridAlphaSweepTest {

	private List<SimilarityEvaluationTest.QA> dataset;
	private TfIdfSimilarityCalculator tfidfCalculator;
	private EmbeddingSimilarityCalculator embeddingCalculator;

	private static final double THRESHOLD = 0.6;

	@BeforeEach
	void setUp() {
		String token = "token";

		tfidfCalculator = new TfIdfSimilarityCalculator();
		embeddingCalculator = new EmbeddingSimilarityCalculator(token);

		// ✅ 여기서는 간단히 3세트만, 실제로는 10세트 이상 사용 권장
		dataset = List.of(
			new SimilarityEvaluationTest.QA(
				"스택은 LIFO 구조로, 마지막에 들어온 데이터가 먼저 나갑니다.",
				List.of("가장 최근에 넣은 게 제일 먼저 빠져요.", "큐는 먼저 들어간 게 먼저 나오지 않나요?", "HTTP 상태 코드에는 200 OK가 있어요.")
			),
			new SimilarityEvaluationTest.QA(
				"큐는 FIFO 구조로, 먼저 들어온 데이터가 먼저 나갑니다.",
				List.of("먼저 줄 선 사람이 먼저 나가는 구조예요.", "스택처럼 나중에 들어간 게 먼저 빠지나요?", "DNS는 도메인 이름을 IP로 바꿔줘요.")
			),
			new SimilarityEvaluationTest.QA(
				"해시 테이블은 키를 해시 함수로 변환해 배열 인덱스로 사용하는 자료구조입니다.",
				List.of("키를 숫자로 바꿔 빠르게 찾는 구조예요.", "B-Tree는 디스크 접근을 줄이는 트리 구조예요.", "스레드는 프로세스 내 실행 흐름이에요.")
			)
		);
	}

	@Test
	@DisplayName("Hybrid α 값을 0.1~0.9까지 sweep하여 성능 비교")
	void sweepAlphaValues() {
		System.out.println("alpha,Top1,MRR,nDCG,Precision,Recall,F1,AvgMs,QPS");

		for (double alpha = 0.1; alpha < 1.0; alpha += 0.1) {
			HybridSimilarityCalculator hybrid =
				new HybridSimilarityCalculator(tfidfCalculator, embeddingCalculator, alpha);

			Metrics metrics = evaluate(hybrid);
			System.out.printf("%.1f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f%n",
				alpha,
				metrics.top1, metrics.mrr, metrics.ndcg,
				metrics.precision, metrics.recall, metrics.f1,
				metrics.avgMs, metrics.qps
			);
		}
	}

	private Metrics evaluate(SimilarityCalculator calculator) {
		int correctTop1 = 0;
		double mrrSum = 0.0;
		double ndcgSum = 0.0;

		int tp = 0;
		int fp = 0;
		int fn = 0;
		long totalTime = 0;

		for (SimilarityEvaluationTest.QA qa : dataset) {
			long start = System.nanoTime();

			List<SimilarityEvaluationTest.Scored> scoredList = new ArrayList<>();
			for (String candidate : qa.candidates()) {
				double score = calculator.calculate(qa.solution(), candidate);
				scoredList.add(new SimilarityEvaluationTest.Scored(candidate, score));
			}

			scoredList.sort((a, b) -> Double.compare(b.score(), a.score()));
			long elapsed = System.nanoTime() - start;
			totalTime += elapsed;

			// Top-1
			if (scoredList.get(0).text().equals(qa.candidates().get(0))) {
				correctTop1++;
			}

			// Precision / Recall with threshold
			for (SimilarityEvaluationTest.Scored s : scoredList) {
				boolean pred = s.score() >= THRESHOLD;
				boolean actual = s.text().equals(qa.candidates().get(0));

				if (pred && actual) {
					tp++;
				} else if (pred) {
					fp++;
				} else if (actual) {
					fn++;
				}
			}

			// Ranking metrics
			for (int rank = 0; rank < scoredList.size(); rank++) {
				if (scoredList.get(rank).text().equals(qa.candidates().get(0))) {
					mrrSum += 1.0 / (rank + 1);
					ndcgSum += 1.0 / (Math.log(rank + 2) / Math.log(2));
					break;
				}
			}
		}

		int total = dataset.size();
		double top1 = (double)correctTop1 / total;
		double mrr = mrrSum / total;
		double ndcg = ndcgSum / total;

		double precision = tp + fp == 0 ? 0 : (double)tp / (tp + fp);
		double recall = tp + fn == 0 ? 0 : (double)tp / (tp + fn);
		double f1 = (precision + recall) == 0 ? 0 : 2 * precision * recall / (precision + recall);

		double avgMs = totalTime / 1_000_000.0 / total;
		double qps = 1000.0 / avgMs;

		return new Metrics(top1, mrr, ndcg, precision, recall, f1, avgMs, qps);
	}

	record Metrics(double top1, double mrr, double ndcg, double precision, double recall, double f1, double avgMs, double qps) {
	}
}
