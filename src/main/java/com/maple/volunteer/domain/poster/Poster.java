package com.maple.volunteer.domain.poster;

import com.maple.volunteer.domain.comment.Comment;
import com.maple.volunteer.domain.common.BaseTime;
import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.domain.posterimg.PosterImg;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Poster extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 게시글 ID

    private String title;   // 게시글 제목
    private String content; // 게시글 내용
    private String author;  // 게시글 작성자
    private Integer likeCount;  // 게시글 좋아요 수

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private CommunityUser communityUser;

    @OneToMany(mappedBy = "poster")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "poster")
    private List<PosterImg> posterImgList;

    @OneToMany(mappedBy = "poster")
    private List<Heart> heartList;



    @Builder
    public Poster(String title, String content, String author, Integer likeCount, CommunityUser communityUser) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.likeCount = likeCount;
        this.communityUser = communityUser;
    }

    public void likeIncrease() {
        this.likeCount += 1;
    }

    public void likeDecrease() {
        this.likeCount -= 1;
    }
}
