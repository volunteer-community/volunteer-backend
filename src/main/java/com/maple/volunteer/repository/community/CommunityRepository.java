package com.maple.volunteer.repository.community;

import com.maple.volunteer.domain.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
