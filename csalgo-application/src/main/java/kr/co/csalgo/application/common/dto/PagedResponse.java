package kr.co.csalgo.application.common.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PagedResponse<T> {
	private List<T> content;
	private int currentPage;
	private int totalPages;
	private long totalElements;
	private boolean first;
	private boolean last;

	@Builder
	public PagedResponse(List<T> content, int currentPage, int totalPages, long totalElements, boolean first, boolean last) {
		this.content = content;
		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
		this.first = first;
		this.last = last;
	}
}
