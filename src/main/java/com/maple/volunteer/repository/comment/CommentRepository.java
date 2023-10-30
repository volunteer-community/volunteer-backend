package com.maple.volunteer.repository.comment;

import com.maple.volunteer.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END "
//            + "FROM Comment cm "
//            + "LEFT JOIN p.communityUser cu "
//            + "LEFT JOIN cu.community c "
//            + "WHERE c.id = :communityId")
//    Optional<Boolean> existsByPosterId(Long posterId);
}
