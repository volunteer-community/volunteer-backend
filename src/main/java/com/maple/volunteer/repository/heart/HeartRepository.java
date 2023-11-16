package com.maple.volunteer.repository.heart;

import com.maple.volunteer.domain.heart.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;



public interface HeartRepository extends JpaRepository<Heart, Long> {

    //좋아요 여부 확인
    @Query("SELECT h FROM Heart h " +
            "LEFT JOIN h.communityUser cu " +
            "LEFT JOIN h.poster p " +
            "WHERE cu.id = :communityUserId AND p.id = :posterId")
    Heart findUserAndPoster(@Param("communityUserId") Long communityUserId, @Param("posterId") Long posterId);

    //좋아요 상태 변경
    @Query("UPDATE Heart h " +
            "SET h.status = :status " +
            "WHERE h.id = :id")
    @Modifying(clearAutomatically = true)
    void updateStatus(@Param("id") Long heartId, @Param("status") boolean b);

    @Query("SELECT h " +
            "FROM Heart h " +
            "LEFT JOIN h.communityUser cu " +
            "LEFT JOIN cu.user u " +
            "WHERE u.id = :userId AND h.status = true")
    List<Heart> findHeartByUserId(@Param("userId") Long userId);

    // 유저 ID에 해당하는 좋아요 상태 변경
    @Query("UPDATE Heart h " +
            "SET h.status = :status " +
            "WHERE h.communityUser IN " +
            "(SELECT cu " +
            "   FROM CommunityUser cu " +
            "   WHERE cu.user.id = :userId)")
    @Modifying(clearAutomatically = true)
    void updateStatusByUserId(@Param("userId") Long userId, @Param("status") boolean status);

    // 게시글 ID에 해당하는 좋아요 가져오기
    @Query("SELECT h " +
            "FROM Heart h " +
            "LEFT JOIN h.poster p " +
            "WHERE p.id = :posterId")
    List<Heart> findAllHeartByPosterId(@Param("posterId") Long posterId);

    // 커뮤니티 ID에 해당하는 좋아요 상태 변경
    @Query("UPDATE Heart h " +
            "SET h.status = :status " +
            "WHERE h.communityUser IN " +
            "(SELECT cu " +
            "   FROM CommunityUser cu " +
            "   WHERE cu.community.id = :communityId)")
    @Modifying(clearAutomatically = true)
    void updateStatusByCommunityId(@Param("communityId") Long communityId, @Param("status") boolean status);

    //CommunityUserId에 해당하는 유저가 좋아요한 게시글 개수
    @Query("SELECT COUNT(h) FROM Heart h " +
            "LEFT JOIN h.communityUser cu " +
            "WHERE cu.id = :communityUserId AND h.status = true ")
    Integer countByCommunityUserId(@Param("communityUserId") Long communityUserId);

   //PosterId에 해당되는 heartList 가져오기
    @Query("SELECT h " +
            "FROM Heart h " +
            "LEFT JOIN h.poster p " +
            "WHERE p.id = :posterId AND h.status = true ")
    List<Heart> findHeartListPosterId(@Param("posterId") Long posterId );

    //userId에 해당되는 heartList 가져오기
    @Query("SELECT h " +
            "FROM Heart h " +
            "LEFT JOIN h.communityUser cu " +
            "LEFT JOIN cu.user u " +
            "WHERE u.id = :userId AND h.status = true ")
    List<Heart> findHeartListUserId(@Param("userId") Long userId);

    @Query("SELECT h " +
            "FROM Heart h " +
            "LEFT JOIN h.communityUser cu " +
            "WHERE cu.id = :communityUserId AND h.status = true ")
    List<Heart> findHeartListCommunityUserId(@Param("communityUserId") Long communityUserId);

}

