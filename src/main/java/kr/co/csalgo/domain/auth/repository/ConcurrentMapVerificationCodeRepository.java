package kr.co.csalgo.domain.auth.repository;

import kr.co.csalgo.domain.auth.type.VerificationCodeType;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private String generateKey(VerificationCodeType verificationCodeType, String email) {
        return verificationCodeType + "::" + email;
    }
}
