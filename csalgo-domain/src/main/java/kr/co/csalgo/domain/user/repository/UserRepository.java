package kr.co.csalgo.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.csalgo.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Page<User> findAll(Pageable pageable);

	boolean existsByEmail(String email);

	Optional<User> findByUuid(UUID uuid);

	Optional<User> findByEmail(String email);
}
