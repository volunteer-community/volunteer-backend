package com.maple.volunteer.domain.poster;

import com.maple.volunteer.domain.comment.Comment;
import com.maple.volunteer.domain.common.BaseTime;
import com.maple.volunteer.domain.communityuser.CommunityUser;
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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private CommunityUser communityUser;

    @OneToMany(mappedBy = "poster")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "poster")
    private List<PosterImg> posterImgList;



    @Builder
    public Poster(String title, String content, String author, CommunityUser communityUser) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.communityUser = communityUser;
    }
}
