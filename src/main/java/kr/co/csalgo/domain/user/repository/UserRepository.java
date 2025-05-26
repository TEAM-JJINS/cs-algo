package kr.co.csalgo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.csalgo.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
}
