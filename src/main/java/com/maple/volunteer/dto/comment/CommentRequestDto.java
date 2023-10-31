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

    //TODO : user->author
    private String commentAuthor;

    @Builder
    public CommentRequestDto(String commentContent, String commentAuthor) {
        this.commentContent = commentContent;
        this.commentAuthor = commentAuthor;
    }

    public Comment toEntity(CommunityUser communityUser, Poster poster) {
        return Comment.builder()
                      .content(commentContent)
                      .author(commentContent)
                      .isDelete(false)
                      .communityUser(communityUser)
                      .poster(poster)
                      .build();
    }

}
