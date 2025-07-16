package kr.co.csalgo.domain.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.csalgo.domain.question.entity.Question;
import kr.co.csalgo.domain.question.entity.QuestionResponse;
import kr.co.csalgo.domain.question.repository.QuestionResponseRepository;
import kr.co.csalgo.domain.question.service.QuestionResponseService;
import kr.co.csalgo.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionResponseService Test")
public class QuestionResponseServiceTest {
	@Mock
	private QuestionResponseRepository questionResponseRepository;

	private QuestionResponseService questionResponseService;

	@BeforeEach
	void setUp() {
		questionResponseService = new QuestionResponseService(questionResponseRepository);
	}

	@Test
	@DisplayName("정상적으로 문제 답변 내역을 생성할 수 있다.")
	void testCreateSuccess() {
		Question question = Question.builder().title("TDD란?").build();
		User user = User.builder().email("test@example.com").build();
		String content = "테스트 주도 개발입니다.";
		String messageId = "<original-message-id@example.com>";

		questionResponseService.create(question, user, content, messageId);

		verify(questionResponseRepository).save(any(QuestionResponse.class));
	}

	@Test
	@DisplayName("문제 답변 내역을 조회할 수 있다.")
	void testListSuccess() {
		questionResponseService.list();

		verify(questionResponseRepository).findAll();
	}
}
