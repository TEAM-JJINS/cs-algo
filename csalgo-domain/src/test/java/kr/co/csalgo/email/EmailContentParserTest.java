package kr.co.csalgo.email.parser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import kr.co.csalgo.application.mail.dto.EmailParseResultDto;
import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.email.EmailContentParser;

@ExtendWith(MockitoExtension.class)
class EmailContentParserTest {

	@Mock
	Message message;

	@Test
	@DisplayName("정상적인 답장 메일은 성공적으로 파싱된다")
	void testParseSuccess() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<reply>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("test@email.com")});
		when(message.getSubject()).thenReturn("Re: [CS-ALGO] 제목");
		when(message.getContent()).thenReturn("답변입니다.\n2024년 1월 1일 오전 10:30 작성: 이전 내용");

		EmailParseResultDto result = EmailContentParser.parse(message);

		assertNotNull(result);
		assertEquals("test@email.com", result.getSender());
		assertEquals("제목", result.getTitle());
		assertEquals("답변입니다.", result.getResponse());
	}

	@Test
	@DisplayName("본문이 Multipart일 때 text/plain인 부분을 추출한다")
	void testMultipartPlainTextContent() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<id>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("multi@email.com")});
		when(message.getSubject()).thenReturn("Re: 제목");

		BodyPart part = mock(BodyPart.class);
		when(part.getDisposition()).thenReturn(null);
		when(part.isMimeType("text/plain")).thenReturn(true);
		when(part.getContent()).thenReturn("답변입니다.\n> 인용");

		Multipart multipart = mock(Multipart.class);
		when(multipart.getCount()).thenReturn(1);
		when(multipart.getBodyPart(0)).thenReturn(part);

		when(message.getContent()).thenReturn(multipart);

		EmailParseResultDto result = EmailContentParser.parse(message);
		assertEquals("답변입니다.", result.getResponse());
	}

	@Test
	@DisplayName("Multipart인데 text/plain이 없으면 EMAIL_CONTENT_NOT_FOUND 예외 발생")
	void testMultipartNoTextPlain() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<id>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("fail@email.com")});
		when(message.getSubject()).thenReturn("Re: 제목");

		BodyPart part = mock(BodyPart.class);
		when(part.getDisposition()).thenReturn(null);
		when(part.isMimeType("text/plain")).thenReturn(false);

		Multipart multipart = mock(Multipart.class);
		when(multipart.getCount()).thenReturn(1);
		when(multipart.getBodyPart(0)).thenReturn(part);

		when(message.getContent()).thenReturn(multipart);

		assertThrows(CustomBusinessException.class, () -> EmailContentParser.parse(message));
	}

	@Test
	@DisplayName("제목이 null이면 빈 문자열로 처리된다")
	void testSubjectIsNull() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<id>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("null@email.com")});
		when(message.getSubject()).thenReturn(null);
		when(message.getContent()).thenReturn("답변입니다.");

		EmailParseResultDto result = EmailContentParser.parse(message);
		assertEquals("", result.getTitle());
	}

	@Test
	@DisplayName("제목 파싱 중 예외 발생 시 EMAIL_SUBJECT_PARSE_FAIL 발생")
	void testSubjectParseError() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<id>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("fail@email.com")});
		when(message.getSubject()).thenThrow(new RuntimeException());

		assertThrows(CustomBusinessException.class, () -> EmailContentParser.parse(message));
	}

	@Test
	@DisplayName("보낸 사람 파싱 중 예외 발생 시 EMAIL_SENDER_PARSE_FAIL 발생")
	void testSenderParseError() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<id>"});
		when(message.getFrom()).thenThrow(new RuntimeException());

		assertThrows(CustomBusinessException.class, () -> EmailContentParser.parse(message));
	}

	@Test
	@DisplayName("본문 파싱 중 예외 발생 시 EMAIL_CONTENT_PARSE_FAIL 발생")
	void testContentParseError() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<id>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("fail@email.com")});
		when(message.getSubject()).thenReturn("Re: 제목");
		when(message.getContent()).thenThrow(new RuntimeException());

		assertThrows(CustomBusinessException.class, () -> EmailContentParser.parse(message));
	}

	@Test
	@DisplayName("In-Reply-To가 없으면 null 반환")
	void testIsNotReply() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(null);

		assertNull(EmailContentParser.parse(message));
	}

	@Test
	@DisplayName("본문이 공백 줄만 있어도 trim()되어 빈 문자열로 처리된다")
	void testEmptyResponseTrimmed() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<id>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("space@email.com")});
		when(message.getSubject()).thenReturn("Re: 제목");
		when(message.getContent()).thenReturn("   \n \n \t\n");

		EmailParseResultDto result = EmailContentParser.parse(message);
		assertEquals("", result.getResponse());
	}

	@Test
	@DisplayName("본문이 한 줄 안에 응답 + Original Message가 있을 때 정확히 응답만 추출된다")
	void testSingleLineWithDelimiter() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<reply>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("inline@email.com")});
		when(message.getSubject()).thenReturn("Re: [CS-ALGO] 테스트");
		when(message.getContent()).thenReturn("한 번 테스트 해봤어요 -----Original Message----- 이전 메일");

		EmailParseResultDto result = EmailContentParser.parse(message);

		assertEquals("한 번 테스트 해봤어요", result.getResponse());
	}

	@Test
	@DisplayName("delimiter가 없으면 본문 전체가 응답으로 처리된다")
	void testNoDelimiterReturnsFullContent() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<reply>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("nodelem@email.com")});
		when(message.getSubject()).thenReturn("Re: [CS-ALGO] 질문");
		when(message.getContent()).thenReturn("이건 delimiter가 없는 메일입니다.");

		EmailParseResultDto result = EmailContentParser.parse(message);

		assertEquals("이건 delimiter가 없는 메일입니다.", result.getResponse());
	}

	@Test
	@DisplayName("본문 끝에 delimiter가 있으면 delimiter 앞까지만 응답으로 처리된다")
	void testDelimiterAtEnd() throws Exception {
		when(message.getHeader("In-Reply-To")).thenReturn(new String[] {"<reply>"});
		when(message.getFrom()).thenReturn(new InternetAddress[] {new InternetAddress("end@email.com")});
		when(message.getSubject()).thenReturn("Re: 끝 테스트");
		when(message.getContent()).thenReturn("응답입니다.\n-----Original Message-----");

		EmailParseResultDto result = EmailContentParser.parse(message);

		assertEquals("응답입니다.", result.getResponse());
	}
}
