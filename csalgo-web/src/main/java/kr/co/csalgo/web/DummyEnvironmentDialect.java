package kr.co.csalgo.web;

import java.util.Collections;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class DummyEnvironmentDialect extends AbstractDialect implements IExpressionObjectDialect {
	public DummyEnvironmentDialect() {
		super("DummyEnvironmentDialect");
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new IExpressionObjectFactory() {
			@Override
			public Set<String> getAllExpressionObjectNames() {
				return Collections.singleton("environment");
			}

			@Override
			public Object buildObject(IExpressionContext context, String name) {
				if ("environment".equals(name)) {
					return new Object() {
						public String getProperty(String key) {
							if ("external.resources.usage-url".equals(key)) {
								return "https://preview.example.com";
							}
							return "";
						}
					};
				}
				return null;
			}

			@Override
			public boolean isCacheable(String name) {
				return true;
			}
		};
	}
}
