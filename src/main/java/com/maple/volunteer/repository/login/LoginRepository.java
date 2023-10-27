package com.maple.volunteer.repository.login;

import com.maple.volunteer.domain.login.Login;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, Long> {
    Optional<Login> findByUser_Email(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Login l SET l.refreshToken = :refreshToken WHERE l.id = :id")
    void updateRefreshTokenById(@Param("id") Long id, @Param("refeshToken") String refreshToken);
}
