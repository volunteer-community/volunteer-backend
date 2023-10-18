package com.maple.volunteer.repository.comment;

import com.maple.volunteer.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
