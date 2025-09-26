package kr.co.csalgo.infrastructure.gpt.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GptDto {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Request {
		private String model;
		private List<Message> messages;
		private Double temperature = 0.7;

		@Getter
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class Message {
			private String role;
			private String content;
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
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
			@Builder
			public static class Message {
				private String role;
				private String content;
			}
		}
	}
}
