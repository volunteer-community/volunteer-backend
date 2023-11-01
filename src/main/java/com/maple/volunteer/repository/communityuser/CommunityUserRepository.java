package com.maple.volunteer.repository.communityuser;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.dto.community.CommunityResponseDto;
import com.maple.volunteer.dto.poster.PosterDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.Optional;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {


    @Query("SELECT cu " +
            "FROM CommunityUser cu " +
            "LEFT JOIN cu.community c " +
            "LEFT JOIN cu.user u " +
            "WHERE c.id = :communityId AND u.id = :userId ")
    Optional<CommunityUser> findByUserIdAndCommunityId(@Param("communityId") Long communityId, @Param("userId") Long userId);

    @Query("SELECT cu " +
            "FROM CommunityUser cu " +
            "LEFT JOIN cu.community c " +
            "LEFT JOIN cu.user u " +
            "WHERE c.id = :communityId AND u.id = :userId AND cu.isWithdraw = false ")
    Optional<CommunityUser> findByUserIdAndCommunityIdAndIsWithdraw(@Param("communityId") Long communityId, @Param("userId") Long userId);



    @Query("SELECT cu " +
            "FROM CommunityUser cu " +
            "LEFT JOIN cu.community c " +
            "WHERE c.id = :communityId")
    Optional<CommunityUser> findByCommunityId(@Param("communityId") Long communityId);



    // 커뮤니티 ID에 해당하는 모든 커뮤니티 유저 삭제
    @Query("UPDATE CommunityUser cu " +
            "SET cu.isWithdraw = :status " +
            "WHERE cu.community.id = :communityId ")
    @Modifying(clearAutomatically = true)
    void CommunityUserDelete(@Param("communityId") Long communityId, @Param("status") Boolean status);


    // 유저 ID로 커뮤니티 가져오기(내가 가입한)
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "cg.id AS categoryId, " +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.maxParticipant AS communityMaxParticipant, " +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath) " +
            "FROM CommunityUser cu " +
            "LEFT JOIN cu.user u " +
            "LEFT JOIN cu.community c " +
            "LEFT JOIN c.category cg " +
            "LEFT JOIN c.communityImgList ci " +
            "WHERE u.id = :userId AND cu.isWithdraw = false ")
    Page<CommunityResponseDto> myCommunitySignList(@Param("userId") Long userId, Pageable pageable);

    // 내가 가입한 커뮤니티 개수
    @Query("SELECT count(cu) " +
            "FROM CommunityUser cu " +
            "WHERE cu.user.id = :userId AND cu.isWithdraw = false ")
    Integer myCommunitySignNumber(@Param("userId") Long userId);
}
