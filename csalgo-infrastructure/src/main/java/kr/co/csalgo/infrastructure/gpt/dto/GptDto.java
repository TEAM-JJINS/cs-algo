package kr.co.csalgo.infrastructure.gpt.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GptDto {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Request {
		private String model;
		private List<Message> messages;
		private Double temperature = 0.7;

		@Getter
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Message {
			private String role;    // "system" or "user"
			private String content; // 메시지 텍스트
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private List<Choice> choices;

		@Getter
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Choice {
			private Message message;

			@Getter
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Message {
				private String role;
				private String content;
			}
		}

		public String firstAnswer() {
			return choices != null && !choices.isEmpty()
				? choices.getFirst().getMessage().getContent()
				: null;
		}
	}
}
