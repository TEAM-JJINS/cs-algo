package kr.co.csalgo.domain.user.service;

import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(String email) {
        checkDuplicateEmail(email);
        User user = User.builder()
                .email(email)
                .build();
        userRepository.save(user);
        return user;
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorCode.DUPLICATE_EMAIL.getMessage());
        }
    }
}
