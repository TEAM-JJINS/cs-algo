package kr.co.csalgo.domain.auth.port;

public interface RefreshTokenStore {
	void initFamily(String familyId, String currentJti, long expiresAtEpochMillis);

	boolean rotateIfLatest(String familyId, String presentedJti, String newJti, long newExpiresAtEpochMillis);

	void revokeFamily(String familyId);

	boolean isFamilyRevoked(String familyId);
}
