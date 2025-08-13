package kr.co.csalgo.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageUrlProvider {

	private static String imagesBaseUrl;
	private static String usageUrl;

	public ImageUrlProvider(
		@Value("${external.resources.images-url}") String imagesBaseUrl,
		@Value("${external.resources.usage-url}") String usageUrl
	) {
		ImageUrlProvider.imagesBaseUrl = imagesBaseUrl;
		ImageUrlProvider.usageUrl = usageUrl;
	}

	public static String logo() {
		return imagesBaseUrl + "/logo.png";
	}

	public static String questionIcon() {
		return imagesBaseUrl + "/question.png";
	}

	public static String responseIcon() {
		return imagesBaseUrl + "/response.png";
	}

	public static String solutionIcon() {
		return imagesBaseUrl + "/solution.png";
	}

	public static String usageIcon() {
		return imagesBaseUrl + "/usage.png";
	}

	public static String unsubscriptionIcon() {
		return imagesBaseUrl + "/cancel.png";
	}

	public static String usageUrl() {
		return usageUrl;
	}
}
