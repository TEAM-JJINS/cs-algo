package kr.co.csalgo.infrastructure.similarity;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.csalgo.domain.similarity.SimilarityCalculator;

/**
 * DB solution 컬럼과 사용자 응답(userInput)의 유사도를 측정하여
 * 평균 Cosine Similarity, Precision, Recall, F1 Score를 계산하는 테스트
 */
@Disabled
class SimilarityEvaluationTest {
	private List<QA> dataset;
	private CosineSimilarityCalculator cosineCalculator;
	private TfIdfSimilarityCalculator tfidfCalculator;
	private EmbeddingSimilarityCalculator embeddingCalculator;
	private HybridSimilarityCalculator hybridCalculator;

	private static final double THRESHOLD = 0.6; // 정답 여부 판단 기준

	@BeforeEach
	void setUp() {
		String token = "token";

		cosineCalculator = new CosineSimilarityCalculator();
		tfidfCalculator = new TfIdfSimilarityCalculator();
		embeddingCalculator = new EmbeddingSimilarityCalculator(token);
		hybridCalculator = new HybridSimilarityCalculator(tfidfCalculator, embeddingCalculator, 0.7);

		dataset = List.of(
			new QA(
				"스택은 LIFO 구조로, 마지막에 들어온 데이터가 먼저 나갑니다.",
				List.of("가장 최근에 넣은 게 제일 먼저 빠져요.", "큐는 먼저 들어간 게 먼저 나오지 않나요?", "HTTP 상태 코드에는 200 OK가 있어요.")
			),
			new QA(
				"큐는 FIFO 구조로, 먼저 들어온 데이터가 먼저 나갑니다.",
				List.of("먼저 줄 선 사람이 먼저 나가는 구조예요.", "스택처럼 나중에 들어간 게 먼저 빠지나요?", "DNS는 도메인 이름을 IP로 바꿔줘요.")
			),
			new QA(
				"해시 테이블은 키를 해시 함수로 변환해 배열 인덱스로 사용하는 자료구조입니다.",
				List.of("키를 숫자로 바꿔 빠르게 찾는 구조예요.", "B-Tree는 디스크 접근을 줄이는 트리 구조예요.", "스레드는 프로세스 내 실행 흐름이에요.")
			),
			new QA(
				"트랜잭션은 여러 작업을 하나로 묶어 전부 성공하거나 전부 실패하게 보장하는 단위입니다.",
				List.of("은행 이체처럼 일부만 되면 안 되는 걸 한 묶음으로 처리해요.", "조인은 두 테이블을 합치는 연산이에요.", "TCP는 연결 지향 프로토콜이에요.")
			),
			new QA(
				"DNS는 도메인 이름을 IP 주소로 변환해줍니다.",
				List.of("사람이 기억하기 좋은 이름을 컴퓨터가 쓰는 숫자로 바꿔줘요.", "TLS는 통신 내용을 암호화해요.", "힙은 완전 이진트리 기반 우선순위 큐예요.")
			),
			new QA(
				"HTTP는 무상태(stateless) 프로토콜이라 요청마다 독립적으로 처리됩니다.",
				List.of("서버가 이전 요청을 기억하지 않아요.", "세션은 서버가 상태를 저장하는 방식이에요.", "데드락은 자원 기다리다 멈추는 상황이에요.")
			),
			new QA(
				"데드락은 여러 프로세스가 서로의 자원을 기다리며 멈춰버리는 상태입니다.",
				List.of("서로가 가진 걸 놓지 않아서 아무도 못 나아가요.", "뮤텍스는 한 번에 한 스레드만 접근해요.", "캐시는 자주 쓰는 데이터를 빠르게 불러옵니다.")
			),
			new QA(
				"이진 탐색은 정렬된 배열에서 중간 값을 비교하며 범위를 절반으로 줄여 나갑니다.",
				List.of("정렬된 데이터에서 반씩 줄여가며 찾는 방법이에요.", "DFS는 깊이 우선 탐색이에요.", "HTTP 404는 페이지가 없다는 뜻이에요.")
			),
			new QA(
				"LRU 캐시는 가장 오래 쓰지 않은 데이터를 교체하는 방식입니다.",
				List.of("최근에 사용 안 한 걸 버리고 새 걸 넣어요.", "FIFO는 먼저 들어온 걸 먼저 빼는 방식이에요.", "SSL은 초기 암호화 프로토콜이에요.")
			),
			new QA(
				"동시성은 한 코어에서 번갈아 실행해 동시에 보이는 효과를 주고, 병렬성은 여러 코어에서 실제로 동시에 실행하는 것입니다.",
				List.of("한 CPU에서 왔다 갔다 하는 건 동시성, 여러 CPU가 동시에 도는 건 병렬성이에요.", "멀티스레드는 하나의 프로세스 안 실행 흐름이에요.", "정규화는 중복을 줄이는 설계 방법이에요.")
			)
		);
	}

	@Test
	@DisplayName("유사도 평가 지표를 계산한다 (Threshold 적용)")
	void evaluateSimilarityMetrics() {
		System.out.println("| 방식                       | Top-1 | MRR  | nDCG | Precision | Recall | F1   | 평균 응답 시간 | QPS   |");
		System.out.println("|---------------------------|-------|------|------|-----------|--------|------|----------------|-------|");
		evaluate("Cosine (Baseline)", cosineCalculator);
		evaluate("TF-IDF + Cosine", tfidfCalculator);
		evaluate("Vector (Embedding)", embeddingCalculator);
		evaluate("Hybrid (TF-IDF + Vector)", hybridCalculator);
	}

	private void evaluate(String name, SimilarityCalculator calculator) {
		int correctTop1 = 0;
		double mrrSum = 0.0;
		double ndcgSum = 0.0;

		// Precision/Recall/F1
		int tp = 0;
		int fp = 0;
		int fn = 0;
		long totalTime = 0;

		for (QA qa : dataset) {
			long start = System.nanoTime();

			// 각 candidate 점수 계산
			List<Scored> scoredList = new ArrayList<>();
			for (String candidate : qa.candidates()) {
				double score = calculator.calculate(qa.solution(), candidate);
				scoredList.add(new Scored(candidate, score));
			}

			scoredList.sort((a, b) -> Double.compare(b.score, a.score));
			long elapsed = System.nanoTime() - start;
			totalTime += elapsed;

			// Top-1 Accuracy + Precision/Recall with threshold
			Scored top1 = scoredList.get(0);
			if (top1.text.equals(qa.candidates().get(0)) && top1.score >= THRESHOLD) {
				correctTop1++;
				tp++;
			} else if (!top1.text.equals(qa.candidates().get(0)) && top1.score >= THRESHOLD) {
				fp++;
			} else if (top1.text.equals(qa.candidates().get(0)) && top1.score < THRESHOLD) {
				fn++;
			}

			// MRR & nDCG (순위 기반 → threshold와 무관)
			for (int rank = 0; rank < scoredList.size(); rank++) {
				if (scoredList.get(rank).text.equals(qa.candidates().get(0))) {
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

		System.out.printf(
			"| %-25s | %.2f | %.2f | %.2f | %.2f | %.2f | %.2f | %.2fms | %.2f QPS |\n",
			name, top1, mrr, ndcg, precision, recall, f1, avgMs, qps
		);
	}

	record QA(String solution, List<String> candidates) {
	}

	record Scored(String text, double score) {
	}
}
