package kr.co.csalgo.web;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class TemplateRenderer {

	private static final Map<String, String> REPLACEMENTS = Map.of(
		"@environment.getProperty('external.resources.usage-url')", "https://preview.example.com"
	);

	public static void main(String[] args) throws Exception {
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setTemplateMode("HTML");

		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(resolver);
		engine.setLinkBuilder(new DummyLinkBuilder());

		// 공통 Context
		Context ctx = new Context();
		ctx.setVariable("username", "게스트");
		ctx.setVariable("navbarTitle", "Preview Mode");

		// 🔹 contentFragment 기본 mock 값 (빈 fragment로 fallback)
		ctx.setVariable("contentFragment", "fragments/common/empty :: empty");

		Path templatesRoot = Paths.get("src/main/resources/templates");
		List<Path> templateFiles = Files.walk(templatesRoot)
			.filter(p -> p.toString().endsWith(".html"))
			.collect(Collectors.toList());

		System.out.println("Found " + templateFiles.size() + " templates");

		for (Path templateFile : templateFiles) {
			String relative = templatesRoot.relativize(templateFile).toString();
			String templateName = relative.replace(File.separator, "/").replaceAll("\\.html$", "");

			try {
				String html = engine.process(templateName, ctx);

				// 치환 규칙 적용
				for (var entry : REPLACEMENTS.entrySet()) {
					html = html.replace(entry.getKey(), entry.getValue());
				}

				Path outPath = Paths.get("build/generated-html").resolve(relative);
				Files.createDirectories(outPath.getParent());
				Files.writeString(outPath, html,
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

				System.out.println("Rendered: " + outPath);
			} catch (Exception e) {
				System.out.println("⚠️ Failed to render " + templateName + ": " + e.getMessage());
				// 실패 시 원본 파일을 그대로 복사 (깨진 화면이라도 확인 가능)
				Path outPath = Paths.get("build/generated-html").resolve(relative);
				Files.createDirectories(outPath.getParent());
				Files.copy(templateFile, outPath, StandardCopyOption.REPLACE_EXISTING);
			}
		}

		System.out.println("✅ All templates rendered (with fallback) to build/generated-html/");
	}
}
