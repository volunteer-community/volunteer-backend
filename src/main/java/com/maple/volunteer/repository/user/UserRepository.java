package com.maple.volunteer.repository.user;

import com.maple.volunteer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
