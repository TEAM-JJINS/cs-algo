package kr.co.csalgo.auth.generator;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class SecureVerificationCodeGeneratorTest {

	private final SecureVerificationCodeGenerator generator = new SecureVerificationCodeGenerator();

	@Test
	@DisplayName("생성된 인증 코드는 6자리 숫자 문자열이어야 한다")
	void testGenerateCodeFormat() {
		String code = generator.generate();
		assertThat(code).hasSize(6);
		assertThat(code).matches("\\d{6}");
	}

	@RepeatedTest(100)
	@DisplayName("여러 번 생성해도 인증 코드는 항상 100000 ~ 999999 범위 내 숫자")
	void testGenerateCodeRangeRepeatedly() {
		String code = generator.generate();
		int value = Integer.parseInt(code);
		assertThat(value).isBetween(100000, 999999);
	}
}
