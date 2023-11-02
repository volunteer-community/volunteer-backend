package com.maple.volunteer.repository.poster;

import com.maple.volunteer.domain.poster.Poster;

import com.maple.volunteer.dto.poster.PosterDetailResponseDto;
import com.maple.volunteer.dto.poster.PosterResponseDto;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface PosterRepository extends JpaRepository<Poster, Long> {

    // 게시글 전체 조회
    @Query("SELECT NEW com.maple.volunteer.dto.poster.PosterResponseDto(" +
            "p.id AS posterId, " +
            "p.title AS posterTitle, " +
            "p.content AS posterContent, " +
            "p.author AS posterAuthor, " +
            "p.heartCount AS heartCount, " +
            "pi.imagePath AS posterImgPath, " +
            "u.profileImg AS profileImg) " +
            "FROM Poster p " +
            "LEFT JOIN p.posterImgList pi "+
            "LEFT JOIN p.communityUser cu " +
            "LEFT JOIN cu.community c " +
            "LEFT JOIN cu.user u " +
            "WHERE c.id = :communityId AND p.isDelete = false")
    Page<PosterResponseDto> findAllPosterList(@Param("communityId") Long communityId, Pageable pageable);


    // 게시글 상세조회
    @Query("SELECT NEW com.maple.volunteer.dto.poster.PosterDetailResponseDto(" +
            "p.id AS posterId, " +
            "p.title AS posterTitle, " +
            "p.author AS posterAuthor, " +
            "p.content AS posterContent, " +
            "p.heartCount AS heartCount, " +
            "pi.imagePath AS posterImgPath, " +
            "u.profileImg AS profileImg) " +
            "FROM Poster p " +
            "LEFT JOIN p.posterImgList pi " +
            "LEFT JOIN p.communityUser cu " +
            "LEFT JOIN cu.community c " +
            "LEFT JOIN cu.user u " +
            "WHERE c.id = :communityId AND p.id = :posterId")
    Optional<PosterDetailResponseDto> findPosterDetailByCommunityIdAndPosterId(@Param("communityId") Long communityId, @Param("posterId") Long posterId);


    // 게시글이 존재 여부 확인
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END "
            + "FROM Poster p "
            + "LEFT JOIN p.communityUser cu "
            + "LEFT JOIN cu.community c "
            + "WHERE c.id = :communityId AND p.isDelete = false")
    Optional<Boolean> existsByCommunityId(@Param("communityId") Long communityId);


    // 커뮤니티 ID에 해당하는 모든 게시글 삭제
    @Query("UPDATE Poster p " +
            "SET p.isDelete = :status " +
            "WHERE p.communityUser " +
            "IN " +
            "(SELECT cu " +
            "   FROM CommunityUser cu " +
            "   WHERE cu.community.id = :communityId)")
    @Modifying(clearAutomatically = true)
    void PosterDeleteByCommunityId(@Param("communityId") Long communityId, @Param("status") Boolean status);

    // 유저 ID에 해당하는 모든 게시글 삭제
    @Query("UPDATE Poster p " +
            "SET p.isDelete = :status " +
            "WHERE p.communityUser " +
            "IN " +
            "(SELECT cu " +
            "   FROM CommunityUser cu " +
            "   WHERE cu.user.id =:userId)")
    @Modifying(clearAutomatically = true)
    void PosterDeleteByUserId(@Param("userId") Long userId, @Param("status") Boolean status);

    // 좋아요 개수 증가
    @Query("UPDATE Poster p " +
            "SET p.heartCount = p.heartCount +1" +
            "WHERE p.id = :posterId")
    @Modifying(clearAutomatically = true)
    void updateHeartCountIncrease(@Param("posterId") Long posterId);


    // 좋아요 개수 감소
    @Query("UPDATE Poster p " +
            "SET p.heartCount = CASE WHEN p.heartCount > 0 " +
            "THEN (p.heartCount -1) " +
            "ELSE 0 END " +
            "WHERE p.id = :posterId")
    @Modifying(clearAutomatically = true)
    void updateHeartCountDecrease(@Param("posterId") Long posterId);


    // 게시글 posterId에 해당 되는 글만 삭제
    @Query("UPDATE Poster p " +
            "SET p.isDelete = true " +
            "WHERE p.id = :posterId")
    @Modifying(clearAutomatically = true)
    void posterDeleteByPosterId(@Param("posterId") Long posterId);

    @Query("SELECT p " +
            "FROM Poster p " +
            "WHERE p.id = :posterId AND p.isDelete = false ")
    Optional<Poster> findByIdAndIsDelete(@Param("posterId") Long posterId);

    @Query("SELECT p " +
            "FROM Poster p " +
            "LEFT JOIN p.communityUser cu " +
            "LEFT JOIN cu.user u " +
            "WHERE u.id = :userId AND cu.isWithdraw = false ")
    List<Poster> findByCommunityUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(p.heartCount) " +
            "FROM Poster p " +
            "WHERE p.id = :posterId AND p.isDelete = false ")
    Integer countByPosterId(@Param("posterId") Long posterId);

    // 마이페이지 - 좋아요 받은 게시글 개수
    @Query("SELECT SUM(p.heartCount)" +
            "FROM Poster p " +
            "WHERE p.communityUser.id IN " +
            "(SELECT cu.id FROM CommunityUser cu " +
            "WHERE cu.user.id = :userId AND cu.isWithdraw = false)")
    Integer numberOfLikedPoster(@Param("userId") Long userId);

}
