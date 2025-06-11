package kr.co.csalgo.domain.email.parser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import kr.co.csalgo.application.mail.dto.EmailParseResultDto;
import kr.co.csalgo.common.exception.CustomBusinessException;

@ExtendWith(MockitoExtension.class)
class EmailContentParserTest {

	@Mock
	Message message;

	@Test
	@DisplayName("Re: 답장이며 본문이 평문이면 성공적으로 파싱된다")
	void testExecuteSuccess() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<original@mail>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("test@email.com")});
		when(message.getSubject()).thenReturn("Re: [CS-ALGO] 질문제목");
		when(message.getContent()).thenReturn("이것은 답변입니다.\n2024년 1월 1일 작성: 이전 내용");

		EmailParseResultDto result = EmailContentParser.parse(message);

		assertNotNull(result);
		assertEquals("test@email.com", result.getSender());
		assertEquals("질문제목", result.getTitle());
		assertEquals("이것은 답변입니다.", result.getResponse());
	}

	@Test
	@DisplayName("In-Reply-To 헤더가 없으면(답장이 아니면) null을 반환하고, 파싱 및 저장을 실행하지 않는다.")
	void testInReplyFalse() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(null);

		EmailParseResultDto result = EmailContentParser.parse(message);

		assertNull(result);
	}

	@Test
	@DisplayName("본문 파싱 실패 시 예외가 발생한다")
	void testExtractResponseFailure() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<id>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("test@email.com")});
		when(message.getSubject()).thenReturn("Re: 테스트제목");
		when(message.getContent()).thenThrow(new RuntimeException("파싱 실패"));

		assertThrows(CustomBusinessException.class, () -> EmailContentParser.parse(message));
	}
}
