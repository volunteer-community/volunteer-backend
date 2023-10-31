package com.maple.volunteer.repository.user;

import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String userEmail);
    User findByPhoneNumber(String phoneNumber);
    User findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND u.email = :email")
    Optional<User> findActiveUserByEmail(@Param("email") String email);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false  AND u.nickname = :nickname")
    Optional<User> findNickname(@Param("nickname") String nickname);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false  AND u.phoneNumber = :phone")
    Optional<User> findPhone(@Param("phone") String phone);
}
