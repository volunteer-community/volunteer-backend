package com.maple.volunteer.repository.comment;

import com.maple.volunteer.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END "
//            + "FROM Comment cm "
//            + "LEFT JOIN p.communityUser cu "
//            + "LEFT JOIN cu.community c "
//            + "WHERE c.id = :communityId")
//    Optional<Boolean> existsByPosterId(Long posterId);

    // 커뮤니티 ID에 해당하는 댓글 삭제
    @Query("UPDATE Comment c " +
            "SET c.isDelete = true " +
            "WHERE c.communityUser.community.id = :communityId ")
    @Modifying(clearAutomatically = true)
    void CommentDeleteByCommunityId(@Param("communityId") Long communityId);

    // 유저 ID에 해당하는 댓글 삭제
    @Query("UPDATE Comment c " +
            "SET c.isDelete = true " +
            "WHERE c.communityUser.user.id = :userId ")
    @Modifying(clearAutomatically = true)
    void CommentDeleteByUserId(@Param("userId") Long userId);
}
