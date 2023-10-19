package com.maple.volunteer.repository.communityimg;

import com.maple.volunteer.domain.communityimg.CommunityImg;
import com.maple.volunteer.dto.community.CommunityImgResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityImgRepository extends JpaRepository<CommunityImg, Long> {

    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityImgResponseDto(" +
            "ci.imageNum as communityImgNum," +
            "ci.imagePath as communityImgPath) " +
            "FROM CommunityImg ci " +
            "LEFT JOIN ci.community c " +
            "WHERE c.id = :communityId ")
    List<CommunityImgResponseDto> findCommunityImgListByCommunityId(@Param("communityId") Long communityId);
}
