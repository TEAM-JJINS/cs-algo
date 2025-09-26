package kr.co.csalgo.infrastructure.gpt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import lombok.Getter;

@Component
@Getter
public class FeedbackPromptProvider {

	private final String systemPrompt;
	private final String userPromptTemplate;

	public FeedbackPromptProvider() {
		this.systemPrompt = loadResource("prompts/system-prompt.txt");
		this.userPromptTemplate = loadResource("prompts/user-prompt.txt");
	}

	public String buildUserPrompt(String question, String solution, String userAnswer, double simScore) {
		return String.format(userPromptTemplate, question, solution, userAnswer, simScore);
	}

	private String loadResource(String path) {
		try {
			ClassPathResource resource = new ClassPathResource(path);
			try (InputStream in = resource.getInputStream()) {
				return new String(in.readAllBytes(), StandardCharsets.UTF_8);
			}
		} catch (IOException e) {
			throw new CustomBusinessException(ErrorCode.FILE_NOT_FOUND);
		}
	}

}
