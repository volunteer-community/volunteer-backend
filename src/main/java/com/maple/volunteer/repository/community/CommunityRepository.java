package com.maple.volunteer.repository.community;

import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.dto.community.CommunityDetailResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityDetailResponseDto(" +
            "c.id as communityId, " +
            "c.title as communityTitle," +
            "c.participant as communityParticipant," +
            "c.author as communityAuhtor," +
            "c.status as communityStatus," +
            "c.content as communityContent) " +
            "FROM Community c " +
            "WHERE c.id = :communityId ")
    Optional<CommunityDetailResponseDto> findCommunityDetailByCommunityId(@Param("communityId") Long communityId);

}
