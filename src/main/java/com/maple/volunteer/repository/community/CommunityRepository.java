package com.maple.volunteer.repository.community;

import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.dto.community.CommunityDetailResponseDto;
import com.maple.volunteer.dto.community.CommunityResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityDetailResponseDto(" +
            "c.id AS communityId, " +
            "c.title AS communityTitle," +
            "c.participant AS communityParticipant," +
            "c.author AS communityAuhtor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent) " +
            "FROM Community c " +
            "WHERE c.id = :communityId ")
    Optional<CommunityDetailResponseDto> findCommunityDetailByCommunityId(@Param("communityId") Long communityId);


    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.introduce AS communityIntroduce," +
            "ci.imagePath AS communityMainImgPath)" +
            "FROM Community c " +
            "LEFT JOIN c.communityImgList ci " +
            "WHERE ci.imageNum = 1 ")
    Page<CommunityResponseDto> findAllCommunityList(Pageable pageable);


}
