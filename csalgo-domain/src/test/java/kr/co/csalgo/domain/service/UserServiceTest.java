package kr.co.csalgo.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.repository.UserRepository;
import kr.co.csalgo.domain.user.service.UserService;

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
	@DisplayName("중복되지 않은 이메일로 사용자를 생성한다.")
	void testUserCreateSuccess() {
		// given
		String email = "new.user@example.com";
		when(userRepository.existsByEmail(email)).thenReturn(false);

		// when
		User user = userService.create(email);

		// then
		assertEquals(email, user.getEmail());
		verify(userRepository).save(any(User.class));
	}

	@Test
	@DisplayName("중복된 이메일로 사용자 생성 시 예외가 발생한다.")
	void testUserCreateDuplicateEmail() {
		// given
		String email = "duplicate@example.com";
		when(userRepository.existsByEmail(email)).thenReturn(true);

		// when & then
		assertThrows(CustomBusinessException.class, () -> {
			userService.create(email);
		});
		verify(userRepository, never()).save(any());
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
	@DisplayName("존재하는 사용자를 id로 성공적으로 삭제한다.")
	void testUserDeleteByUserIdSuccess() {
		Long userId = 1L;
		String email = "team.jjins@gmail.com";
		User user = new User(email);

		ReflectionTestUtils.setField(user, "id", userId);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userRepository.existsByEmail(email)).thenReturn(false);

		userService.delete(userId);

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

	@Test
	@DisplayName("존재하는 ID로 사용자를 조회할 수 있다.")
	void testReadUserSuccess() {
		// given
		Long userId = 1L;
		String email = "read@example.com";

		User user = User.builder()
			.email(email)
			.build();
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// when
		User result = userService.read(userId);

		// then
		assertNotNull(result);
		assertEquals(email, result.getEmail());
		verify(userRepository).findById(userId);
	}

	@Test
	@DisplayName("존재하지 않는 ID로 사용자 조회 시 예외가 발생한다.")
	void testReadUserNotFound() {
		// given
		Long invalidUserId = 999L;
		when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

		// when & then
		assertThrows(CustomBusinessException.class, () -> userService.read(invalidUserId));
		verify(userRepository).findById(invalidUserId);
	}

	@Test
	@DisplayName("존재하는 이메일로 사용자를 조회할 수 있다.")
	void testReadUserByEmailSuccess() {
		String email = "read@example.com";

		User user = User.builder()
			.email(email)
			.build();

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		User result = userService.read(email);
		assertNotNull(result);
		assertEquals(email, result.getEmail());
		verify(userRepository).findByEmail(email);
	}

	@Test
	@DisplayName("존재하지 않는 이메일로 사용자 조회 시 예외가 발생한다.")
	void testReadUserByEmailNotFound() {
		String invalidUserEmail = "noone@email.com";
		when(userRepository.findByEmail(invalidUserEmail)).thenReturn(Optional.empty());

		assertThrows(CustomBusinessException.class, () -> userService.read(invalidUserEmail));
		verify(userRepository).findByEmail(invalidUserEmail);
	}

	@Test
	@DisplayName("전체 사용자 목록을 반환한다.")
	void testListUsers() {
		// given
		List<User> users = List.of(
			User.builder().email("user1@example.com").build(),
			User.builder().email("user2@example.com").build()
		);
		when(userRepository.findAll()).thenReturn(users);

		// when
		List<User> result = userService.list();

		// then
		assertEquals(2, result.size());
		assertEquals("user1@example.com", result.get(0).getEmail());
		assertEquals("user2@example.com", result.get(1).getEmail());
		verify(userRepository).findAll();
	}

	@Test
	@DisplayName("페이지 정보에 따라 사용자 목록을 페이징하여 조회할 수 있다.")
	void testUserListWithPaging() {
		// given
		int page = 0;
		int size = 2;
		Pageable pageable = PageRequest.of(page, size);

		List<User> users = List.of(
			User.builder().email("user1@example.com").build(),
			User.builder().email("user2@example.com").build()
		);
		Page<User> mockPage = new PageImpl<>(users, pageable, 5);

		when(userRepository.findAll(pageable)).thenReturn(mockPage);

		// when
		Page<User> result = userService.list(pageable);

		// then
		assertEquals(2, result.getContent().size());         // 현재 페이지 데이터 개수
		assertEquals("user1@example.com", result.getContent().get(0).getEmail());
		assertEquals(5, result.getTotalElements());          // 전체 데이터 개수
		assertEquals(3, result.getTotalPages());             // 총 페이지 수 (ceil(5/2) = 3)
		assertTrue(result.hasNext());                        // 다음 페이지 존재 여부
		verify(userRepository).findAll(pageable);
	}
}
