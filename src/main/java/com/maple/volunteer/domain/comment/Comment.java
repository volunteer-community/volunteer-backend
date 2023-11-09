package com.maple.volunteer.domain.comment;

import com.maple.volunteer.domain.common.BaseTime;
import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.poster.Poster;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 ID

    @Lob
    @Column(nullable = false)
    private String content; // 댓글 내용
    @Column(nullable = false)
    private String author; // 작성자
    @Column(nullable = false)
    private Boolean isDelete;   // 댓글 삭제 유무


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_user_id")
    private CommunityUser communityUser;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Poster poster;

    @Builder
    public Comment(String content, String author, Boolean isDelete, CommunityUser communityUser, Poster poster) {
        this.content = content;
        this.author = author;
        this.isDelete = isDelete;
        this.communityUser = communityUser;
        this.poster = poster;
    }
}
