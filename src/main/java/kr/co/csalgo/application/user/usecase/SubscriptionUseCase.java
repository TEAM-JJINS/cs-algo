package kr.co.csalgo.application.user.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.csalgo.application.user.dto.SubscriptionUseCaseDto;
import kr.co.csalgo.application.user.dto.UnsubscriptionUseCaseDto;
import kr.co.csalgo.user.entity.User;
import kr.co.csalgo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionUseCase {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public SubscriptionUseCaseDto.Response create(SubscriptionUseCaseDto.Request request) {
		log.info("[구독 요청] email={}", request.getEmail());

		User user = userService.create(request.getEmail());

		log.info("[구독 완료] userId={}, email={}, uuid={}", user.getId(), user.getEmail(), user.getUuid());

		return SubscriptionUseCaseDto.Response.of();
	}

	public UnsubscriptionUseCaseDto.Response unsubscribe(UnsubscriptionUseCaseDto.Request request) {
		log.info("[구독 해지 요청] userId={}", request.getUserId());

		userService.delete(request.getUserId());

		log.info("[구독 해지 완료] userId={}", request.getUserId());

		return UnsubscriptionUseCaseDto.Response.of();
	}
}
