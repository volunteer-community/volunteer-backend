package com.maple.volunteer.repository.login;

import com.maple.volunteer.domain.login.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
}
