package com.maple.volunteer.dto.comment;

import com.maple.volunteer.domain.comment.Comment;
import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.poster.Poster;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private String commentContent;


    @Builder
    public CommentRequestDto(String commentContent) {
        this.commentContent = commentContent;

    }



}
