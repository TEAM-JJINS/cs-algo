package kr.co.csalgo.domain.auth.port;

public interface PasswordHasher {
	String encode(String raw);

	boolean matches(String raw, String encoded);
}
