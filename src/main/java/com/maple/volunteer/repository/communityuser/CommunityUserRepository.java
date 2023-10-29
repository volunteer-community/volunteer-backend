package com.maple.volunteer.repository.communityuser;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {

    @Query("SELECT cu " +
            "FROM CommunityUser cu " +
            "WHERE cu.community.id = :communityId AND cu.user.id = :userId ")
    Optional<CommunityUser> findByUserIdAndCommunityId(@Param("communityId") Long communityId, @Param("userId") Long userId);
}
