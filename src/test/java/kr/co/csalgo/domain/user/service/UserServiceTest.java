package kr.co.csalgo.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.repository.UserRepository;

@DisplayName("UserService Test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	private UserRepository userRepository;
	private UserService userService;

	@BeforeEach
	void setUp() {
		userService = new UserService(userRepository);
	}

	@Test
	@DisplayName("존재하는 사용자를 성공적으로 삭제한다.")
	void testUserDeleteSuccess() {
		// given
		String email = "team.jjins@gmail.com";
		UUID uuid = UUID.randomUUID();

		User user = new User(email);
		ReflectionTestUtils.setField(user, "uuid", uuid);

		when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
		when(userRepository.existsByEmail(email)).thenReturn(false);

		// when
		userService.delete(user.getUuid());

		// then
		assertFalse(userRepository.existsByEmail(email));
		verify(userRepository).delete(user);
	}

	@Test
	@DisplayName("존재하지 않는 사용자를 삭제할 때 예외가 발생한다.")
	void testUserDeleteFail() {
		// given
		UUID unregisteredUuid = UUID.randomUUID();
		when(userRepository.findByUuid(unregisteredUuid)).thenReturn(Optional.empty());

		// when
		assertThrows(CustomBusinessException.class, () -> {
			userService.delete(unregisteredUuid);
		});
	}
}
