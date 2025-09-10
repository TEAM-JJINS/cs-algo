package kr.co.csalgo.web;

import java.util.Map;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.linkbuilder.ILinkBuilder;

public class DummyLinkBuilder implements ILinkBuilder {
	@Override
	public String getName() {
		return "DUMMY";
	}

	@Override
	public Integer getOrder() {
		return 0;
	}

	@Override
	public String buildLink(IExpressionContext context, String base, Map<String, Object> parameters) {
		// 그냥 원문 그대로 반환
		return base;
	}
}
