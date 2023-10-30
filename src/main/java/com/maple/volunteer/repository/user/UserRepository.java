package com.maple.volunteer.repository.user;

import com.maple.volunteer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String userEmail);
    User findByPhoneNumber(String phoneNumber);
    User findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND u.email = :email")
    Optional<User> findActiveUserByEmail(@Param("email") String email);
}
