package kr.co.csalgo.domain.user.service;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        User user = new User(email);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        userService.delete(user.getId());

        // then
        assertFalse(userRepository.existsByEmail(email));
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 삭제할 때 예외가 발생한다.")
    void testUserDeleteFail() {
        // given
        Long unregisteredUserId = 999L;
        when(userRepository.findById(unregisteredUserId)).thenReturn(Optional.empty());

        // when
        assertThrows(CustomBusinessException.class, () -> {
            userService.delete(unregisteredUserId);
        });
    }
}