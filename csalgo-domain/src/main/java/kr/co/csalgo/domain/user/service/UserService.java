package kr.co.csalgo.domain.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.co.csalgo.common.exception.CustomBusinessException;
import kr.co.csalgo.common.exception.ErrorCode;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

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

	public User read(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new CustomBusinessException(ErrorCode.USER_NOT_FOUND));
	}

	public User read(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomBusinessException(ErrorCode.USER_NOT_FOUND));
	}

	public void delete(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomBusinessException(ErrorCode.USER_NOT_FOUND));
		userRepository.delete(user);
	}

	public void delete(UUID uuid) {
		User user = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomBusinessException(ErrorCode.USER_NOT_FOUND));
		userRepository.delete(user);
	}

	public Page<User> list(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	public List<User> list() {
		return userRepository.findAll();
	}

	private void checkDuplicateEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new CustomBusinessException(ErrorCode.DUPLICATE_EMAIL);
		}
	}
}
