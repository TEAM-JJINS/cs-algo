package kr.co.csalgo.infrastructure.auth.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import kr.co.csalgo.domain.auth.repository.VerificationCodeRepository;
import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConcurrentMapVerificationCodeRepository implements VerificationCodeRepository {

	private final ConcurrentHashMap<String, String> verificationCodeMap = new ConcurrentHashMap<>();
	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	private final long expireTime;

	@Override
	public void create(String email, String code, VerificationCodeType verificationCodeType) {
		String key = generateKey(verificationCodeType, email);
		verificationCodeMap.put(key, code);
		scheduledExecutorService.schedule(() -> verificationCodeMap.remove(key), expireTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean verify(String email, String code, VerificationCodeType verificationCodeType) {
		String key = generateKey(verificationCodeType, email);
		String storedCode = verificationCodeMap.get(key);
		if (storedCode != null && storedCode.equals(code)) {
			verificationCodeMap.remove(key);
			return true;
		}
		return false;
	}

	private String generateKey(VerificationCodeType verificationCodeType, String email) {
		return verificationCodeType + "::" + email;
	}
}
