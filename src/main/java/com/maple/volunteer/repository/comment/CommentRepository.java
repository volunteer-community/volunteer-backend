package com.maple.volunteer.repository.comment;

import com.maple.volunteer.domain.comment.Comment;
import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.dto.comment.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 댓글 존재 여부 확인
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Comment cm " +
            "LEFT JOIN cm.poster p " +
            "WHERE p.id = :posterId AND cm.isDelete = false")
    Optional<Boolean> existsByPosterId(@Param("posterId") Long posterId);

    // 댓글 조회
    @Query("SELECT NEW com.maple.volunteer.dto.comment.CommentResponseDto(" +
            "u.id AS userId, " +
            "cm.id AS commentId, " +
            "cm.content AS commentContent, " +
            "cm.author AS commentAuthor, " +
            "cm.createdAt AS commentCreatedAt, " +
            "cm.updatedAt AS commentUpdatedAt, " +
            "u.profileImg AS profileImg) " +
            "FROM Comment cm " +
            "LEFT JOIN cm.poster p " +
            "LEFT JOIN cm.communityUser cu " +
            "LEFT JOIN cu.user u " +
            "WHERE p.id = :posterId AND cm.isDelete = false")
    Page<CommentResponseDto> findAllCommentList(@Param("posterId") Long posterId, PageRequest pageable);

    // 댓글 수정
    @Query("UPDATE Comment cm " +
            "SET cm.content = :content, " +
            "cm.updatedAt = CURRENT_TIMESTAMP " +
            " WHERE cm.id = :commentId")
    @Modifying(clearAutomatically = true)
    void updateCommentContent(@Param("commentId") Long commentId, @Param("content") String content);


    // 게시글이 삭제 되었을 때 게시글에 해당되는 댓글 전체 삭제
    @Query("UPDATE Comment cm "
            + " SET cm.isDelete = true "
            + " WHERE cm.poster.id = :posterId")
    @Modifying(clearAutomatically = true)
    void commentDeleteByPosterId(@Param("posterId") Long posterId);


    // commentId에 해당되는 댓글만 삭제
    @Query("UPDATE Comment cm "
            + " SET cm.isDelete = true "
            + " WHERE cm.id = :commentId")
    @Modifying(clearAutomatically = true)
    void commentDeleteByCommentId(@Param("commentId") Long commentId);


    // 커뮤니티 ID에 해당하는 댓글 삭제
    @Query("UPDATE Comment c " +
            "SET c.isDelete = :status " +
            "WHERE c.communityUser " +
            "IN " +
            "(SELECT cu " +
            "   FROM CommunityUser cu " +
            "   WHERE cu.community.id = :communityId)")
    @Modifying(clearAutomatically = true)
    void commentDeleteByCommunityId(@Param("communityId") Long communityId, @Param("status") Boolean status);

    // 유저 ID에 해당하는 댓글 삭제
    @Query("UPDATE Comment c " +
            "SET c.isDelete = :status " +
            "WHERE c.communityUser IN " +
            "(SELECT cu " +
            "   FROM CommunityUser cu " +
            "   WHERE cu.user.id = :userId)")
    @Modifying(clearAutomatically = true)
    void commentDeleteByUserId(@Param("userId") Long userId, @Param("status") Boolean status);

    // 게시글 ID로 댓글 가져오기
    @Query("SELECT cm " +
            "FROM Comment cm " +
            "LEFT JOIN cm.poster p " +
            "WHERE p.id = :posterId")
    List<Comment> findAllCommentByPosterId(@Param("posterId") Long posterId);

    // userId 가 작성한 댓글 개수
    @Query("SELECT COUNT(cm) " +
            "FROM Comment cm " +
            "LEFT JOIN cm.communityUser cu " +
            "WHERE cu.id = :communityUserId AND cm.isDelete = false ")
    Integer countByCommunityUserId(@Param("communityUserId") Long communityUserId);

    // commentId에 해당되는 댓글 삭제 여부 확인
    @Query("SELECT cm " +
            "FROM Comment cm " +
            "WHERE cm.id = :commentId AND cm.isDelete = :status ")
    Optional<Comment> findByIdAndIsDelete(@Param("commentId") Long commentId,@Param("status") Boolean status);

    @Query("UPDATE Comment c " +
            "SET c.isDelete = :status " +
            "WHERE c.communityUser " +
            "IN " +
            "(SELECT cu " +
            "   FROM CommunityUser cu " +
            "   WHERE cu.id = :communityUserId)")
    @Modifying(clearAutomatically = true)
    void commentDeleteByCommunityUserId(@Param("communityUserId") Long communityUserId, @Param("status") boolean status);

    @Query("UPDATE Comment c " +
            "SET c.author = :nickname " +
            "WHERE c.author = :oldNickname AND c.isDelete = false")
    @Modifying(clearAutomatically = true)
    void updateCommentNickname(@Param("nickname")String nickname, @Param("oldNickname") String userNickname);

    //userId에 해당되는 commentList 가져오기
    @Query("SELECT cm " +
            "FROM Comment cm " +
            "LEFT JOIN cm.communityUser cu " +
            "LEFT JOIN cu.user u " +
            "WHERE u.id = :userId AND cm.isDelete = false ")
    List<Comment> findCommentListUserId(@Param("userId") Long userId);


    @Query("SELECT cm " +
            "FROM Comment cm " +
            "LEFT JOIN cm.communityUser cu " +
            "WHERE cu.id = :communityUserId AND cm.isDelete = false ")
    List<Comment> findCommentListCommunityUserId(@Param("communityUserId") Long communityUserId);

}
