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

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 사용자 혹은 존재하지 않는 사용자입니다."));
        userRepository.delete(user);
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorCode.DUPLICATE_EMAIL.getMessage());
        }
    }
}
