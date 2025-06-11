package kr.co.csalgo.application.mail.usecase;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.mail.Flags;
import jakarta.mail.Message;
import kr.co.csalgo.application.mail.dto.EmailParseResultDto;
import kr.co.csalgo.domain.email.EmailReceiver;
import kr.co.csalgo.domain.email.parser.EmailContentParser;
import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.service.QuestionResponseService;
import kr.co.csalgo.domain.question.service.QuestionService;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class RegisterQuestionResponseUseCaseTest {
	@Mock
	private EmailReceiver emailReceiver;
	@Mock
	private QuestionResponseService questionResponseService;
	@Mock
	private UserService userService;
	@Mock
	private QuestionService questionService;

	private RegisterQuestionResponseUseCase registerQuestionResponseUseCase;

	@BeforeEach
	void setUp() {
		registerQuestionResponseUseCase = new RegisterQuestionResponseUseCase(
			emailReceiver, questionResponseService, userService, questionService
		);
	}

	@Test
	@DisplayName("메일 파싱 후 응답을 저장하고 메일을 읽음 처리한다")
	void testParseAndSaveSuccess() throws Exception {
		Message message = mock(Message.class);
		when(emailReceiver.receiveMessages()).thenReturn(List.of(message));

		EmailParseResultDto parseResult = new EmailParseResultDto("sender@email.com", "질문제목", "답변내용");
		User user = mock(User.class);
		Question question = mock(Question.class);
		QuestionResponse response = mock(QuestionResponse.class);
		when(response.getId()).thenReturn(1L);

		when(userService.read(parseResult.getSender())).thenReturn(user);
		when(questionService.read(parseResult.getTitle())).thenReturn(question);
		when(questionResponseService.create(question, user, parseResult.getResponse())).thenReturn(response);

		try (MockedStatic<EmailContentParser> mockedParser = mockStatic(EmailContentParser.class)) {
			mockedParser.when(() -> EmailContentParser.parse(message)).thenReturn(parseResult);

			registerQuestionResponseUseCase.execute();

			verify(userService).read(parseResult.getSender());
			verify(questionService).read(parseResult.getTitle());
			verify(questionResponseService).create(question, user, parseResult.getResponse());
			verify(message).setFlag(Flags.Flag.SEEN, true);
		}
	}
}
