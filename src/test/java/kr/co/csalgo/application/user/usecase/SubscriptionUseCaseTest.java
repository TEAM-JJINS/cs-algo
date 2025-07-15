package kr.co.csalgo.application.user.usecase;

import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.csalgo.application.user.dto.SubscriptionUseCaseDto;
import kr.co.csalgo.application.user.dto.UnsubscriptionUseCaseDto;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;

@DisplayName("SubscriptionUseCase Test")
@ExtendWith(MockitoExtension.class)
class SubscriptionUseCaseTest {
	@Mock
	private UserService userService;
	@Mock
	private PasswordEncoder passwordEncoder;
	private SubscriptionUseCase subscriptionUseCase;

	@BeforeEach
	void setUp() {
		subscriptionUseCase = new SubscriptionUseCase(userService, passwordEncoder);
	}

	@Test
	@DisplayName("구독 생성 테스트")
	void testSubscribe() {
		// given
		String email = "test@example.com";
		String password = "originPassword";
		String enPassword = passwordEncoder.encode(password);
		SubscriptionUseCaseDto.Request request = SubscriptionUseCaseDto.Request.builder()
			.email(email)
			.password(enPassword)
			.build();

		User mockUser = User.builder()
			.email(email)
			.password(enPassword)
			.build();
		UUID uuid = UUID.randomUUID();

		ReflectionTestUtils.setField(mockUser, "uuid", uuid);
		ReflectionTestUtils.setField(mockUser, "id", 1L);

		when(userService.create(email, enPassword)).thenReturn(mockUser);

		// when
		SubscriptionUseCaseDto.Response response = subscriptionUseCase.create(request);

		// then
		verify(userService).create(email, enPassword);
	}

	@Test
	@DisplayName("구독 삭제 테스트")
	void testUnsubscribe() {
		// given
		UUID uuid = UUID.randomUUID();
		UnsubscriptionUseCaseDto.Request request = UnsubscriptionUseCaseDto.Request.builder()
			.userId(uuid)
			.build();

		// when
		UnsubscriptionUseCaseDto.Response response = subscriptionUseCase.unsubscribe(request);

		// then
		verify(userService).delete(uuid);
	}
}
