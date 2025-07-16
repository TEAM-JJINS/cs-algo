package kr.co.csalgo.common.logging;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

class TraceIdFilterTest {

	private final TraceIdFilter traceIdFilter = new TraceIdFilter();

	@Test
	void doFilterInternal_shouldSetAndClearTraceId() throws Exception {
		// given
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		// when
		traceIdFilter.doFilterInternal(request, response, new MockFilterChain());

		// then
		assertThat(MDC.get("traceId")).isNull();
	}

	@Test
	void doFilterInternal_shouldPopulateTraceIdDuringFilterChain() throws Exception {
		// given
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		// FilterChain 구현체를 직접 주입해서 필터 체인 도는 동안 assert
		traceIdFilter.doFilterInternal(request, response, new FilterChain() {
			@Override
			public void doFilter(ServletRequest req, ServletResponse res) {
				String traceId = MDC.get("traceId");
				assertThat(traceId).isNotNull();
			}
		});

		// after
		assertThat(MDC.get("traceId")).isNull();
	}
}
