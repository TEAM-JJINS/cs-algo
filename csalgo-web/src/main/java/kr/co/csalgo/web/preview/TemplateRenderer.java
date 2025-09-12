package kr.co.csalgo.web.preview;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemplateRenderer {

	private static final Map<String, String> REPLACEMENTS = Map.of(
		"@environment.getProperty('external.resources.usage-url')", "https://preview.example.com"
	);

	public static void main(String[] args) throws Exception {
		// 템플릿 리졸버 설정
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setTemplateMode("HTML");

		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(resolver);
		engine.setLinkBuilder(new DummyLinkBuilder());

		// 공통 Context 값
		Context ctx = new Context();
		ctx.setVariable("username", "게스트");
		ctx.setVariable("navbarTitle", "Preview Mode");
		ctx.setVariable("contentFragment", "fragments/common/empty :: empty");

		// 템플릿 파일 목록 수집
		Path templatesRoot = Paths.get("src/main/resources/templates");
		List<Path> templateFiles = Files.walk(templatesRoot)
			.filter(p -> p.toString().endsWith(".html"))
			.collect(Collectors.toList());

		log.info("총 {}개의 템플릿 발견", templateFiles.size());

		for (Path templateFile : templateFiles) {
			String relative = templatesRoot.relativize(templateFile).toString();
			String templateName = relative.replace(File.separator, "/").replaceAll("\\.html$", "");

			try {
				// 템플릿 처리
				String html = engine.process(templateName, ctx);

				// 문자열 치환 적용
				for (var entry : REPLACEMENTS.entrySet()) {
					html = html.replace(entry.getKey(), entry.getValue());
				}

				// 결과 파일 저장
				Path outPath = Paths.get("build/generated-html").resolve(relative);
				Files.createDirectories(outPath.getParent());
				Files.writeString(outPath, html,
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

				log.info("렌더링 성공: {}", outPath);

			} catch (Exception e) {
				// 실패 시 원본 파일 복사
				Path outPath = Paths.get("build/generated-html").resolve(relative);
				Files.createDirectories(outPath.getParent());
				Files.copy(templateFile, outPath, StandardCopyOption.REPLACE_EXISTING);
				log.warn("렌더링 실패: {}, 원본 복사 처리 ({})", templateName, e.getMessage());
			}
		}
	}
}
