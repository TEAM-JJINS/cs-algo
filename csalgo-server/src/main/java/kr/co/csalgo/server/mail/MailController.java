package kr.co.csalgo.server.mail;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.csalgo.common.util.MailTemplate;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MailController {

	@GetMapping(value = "/mail-preview", produces = MediaType.TEXT_HTML_VALUE)
	public String preview() {
		return MailTemplate.formatFeedbackMailBody(
			"tester",
			"시간 복잡도와 공간 복잡도를 설명하시오",
			"시간 복잡도는 연산 횟수, 공간 복잡도는 메모리 크기와 관련있습니다.",
			"시간 복잡도는 입력 크기와 연산 횟수의 관계를 나타내고, 공간 복잡도는 추가 메모리 사용량을 의미합니다.",
			82.5,
			"답변은 전반적으로 유사도가 높은 편입니다. 다만 구체적 예시가 부족합니다.",
			List.of("시간 복잡도를 올바르게 언급함", "공간 복잡도의 개념을 이해함"),
			List.of("연산 횟수와 입력 크기의 상관관계 설명 필요", "메모리 증가 설명 보강 필요"),
			List.of("빅-O, 빅-Ω, 빅-Θ의 의미 학습", "정렬 알고리즘별 복잡도 비교")
		);
	}
}

