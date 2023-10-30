package com.maple.volunteer.repository.communityuser;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.dto.poster.PosterDetailResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {


    Optional<CommunityUser> findByCommunityId(Long communityId);

}
