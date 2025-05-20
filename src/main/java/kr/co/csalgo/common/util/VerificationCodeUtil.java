package kr.co.csalgo.common.util;

import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@NoArgsConstructor
public class VerificationCodeUtil {

    public static String generate() {
        SecureRandom secureRandom = new SecureRandom();
        int code = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
