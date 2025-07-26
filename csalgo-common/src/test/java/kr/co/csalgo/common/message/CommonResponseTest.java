package kr.co.csalgo.common.message;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;

class CommonResponseTest {

	@Test
	void testGetMessageSuccess() {
		String expectedMessage = "테스트 메시지";

		CommonResponse response = new CommonResponse(expectedMessage);
		assertThat(response.getMessage()).isEqualTo(expectedMessage);
	}
}
