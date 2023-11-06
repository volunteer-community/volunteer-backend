package com.maple.volunteer.repository.communityimg;

import com.maple.volunteer.domain.communityimg.CommunityImg;
import com.maple.volunteer.dto.community.CommunityImgPathDto;
import com.maple.volunteer.dto.community.CommunityImgResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityImgRepository extends JpaRepository<CommunityImg, Long> {

    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityImgResponseDto(" +
            "ci.imageNum AS communityImgNum," +
            "ci.imagePath AS communityImgPath) " +
            "FROM CommunityImg ci " +
            "LEFT JOIN ci.community c " +
            "WHERE c.id = :communityId ")
    List<CommunityImgResponseDto> findCommunityImgListByCommunityId(@Param("communityId") Long communityId);

    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityImgPathDto(" +
            "ci.imagePath AS communityImgPath) " +
            "FROM CommunityImg ci " +
            "WHERE ci.community.id = :communityId ")
    List<CommunityImgPathDto> findCommunityImgPathList(@Param("communityId") Long communityId);

    // 커뮤니티 img 삭제를 위한 리스트 받아오기
    @Query("SELECT ci " +
            "FROM CommunityImg ci " +
            "WHERE ci.community.id = :communityId ")
    List<CommunityImg> findDeletedCommunityImgPathList(@Param("communityId") Long communityId);

    void deleteByCommunityId(Long communityId);

    // 커뮤니티이미지 isDelete = true
    @Query("UPDATE CommunityImg ci " +
            "SET ci.isDelete = :status " +
            "WHERE ci.id = :communityImgId ")
    @Modifying(clearAutomatically = true)
    void deleteByCommunityImgId(@Param("communityImgId") Long communityImgId ,@Param("status") Boolean status);

}
