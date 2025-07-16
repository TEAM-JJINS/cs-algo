package kr.co.csalgo.infrastructure.auth.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.csalgo.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.auth.type.VerificationCodeType;

class ConcurrentMapVerificationCodeRepositoryTest {

	private VerificationCodeRepository repository;

	@BeforeEach
	void setUp() {
		// 만료 시간 100ms로 설정
		repository = new ConcurrentMapVerificationCodeRepository(100);
	}

	@Test
	@DisplayName("인증 코드를 저장하고 유효한 값으로 검증에 성공할 수 있다")
	void verifyCodeSuccess() {
		String email = "test@example.com";
		String code = "123456";
		VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

		repository.create(email, code, type);
		boolean result = repository.verify(email, code, type);

		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("인증 코드가 일치하지 않으면 검증에 실패한다")
	void verifyCodeFailWhenCodeDoesNotMatch() {
		String email = "test@example.com";
		String correctCode = "123456";
		String wrongCode = "654321";
		VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

		repository.create(email, correctCode, type);
		boolean result = repository.verify(email, wrongCode, type);

		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("인증 코드는 검증 후 삭제되어 재사용할 수 없다")
	void verifyCodeOnlyOnce() {
		String email = "test@example.com";
		String code = "123456";
		VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

		repository.create(email, code, type);
		boolean firstAttempt = repository.verify(email, code, type);
		boolean secondAttempt = repository.verify(email, code, type);

		assertThat(firstAttempt).isTrue();
		assertThat(secondAttempt).isFalse();
	}

	@Test
	@DisplayName("인증 코드는 일정 시간이 지나면 만료되어 검증할 수 없다")
	void verifyCodeFailsAfterExpiration() throws InterruptedException {
		String email = "test@example.com";
		String code = "123456";
		VerificationCodeType type = VerificationCodeType.SUBSCRIPTION;

		repository.create(email, code, type);

		// 만료 시간(100ms)보다 조금 더 기다림
		Thread.sleep(200);

		boolean result = repository.verify(email, code, type);

		assertThat(result).isFalse();
	}
}
