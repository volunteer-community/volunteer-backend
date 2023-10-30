package com.maple.volunteer.repository.user;

import com.maple.volunteer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String userEmail);
    User findByPhoneNumber(String phoneNumber);
    User findByNickname(String nickname);
}
