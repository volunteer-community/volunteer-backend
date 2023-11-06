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
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Poster extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 게시글 ID

    @Column(length = 50)
    @Size(min = 1, max = 50)
    private String title;   // 게시글 제목

    @Lob
    private String content; // 게시글 내용
    private String author;  // 게시글 작성자

    private Boolean isDelete; // 게시글 삭제 유무

    private Integer heartCount;  // 게시글 좋아요 수

    //댓글 개수 추가해야함

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_user_id")
    private CommunityUser communityUser;
    @OneToMany(mappedBy = "poster")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "poster")
    private List<PosterImg> posterImgList;

    @OneToMany(mappedBy = "poster")
    private List<Heart> heartList;


    @Builder
    public Poster(String title, String content, String author, Integer heartCount, Boolean isDelete, CommunityUser communityUser) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.heartCount = heartCount;
        this.isDelete = isDelete;
        this.communityUser = communityUser;
    }

    public void posterUpdate(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    //    public void heartIncrease() {
//        this.heartCount += 1;
//    }
//
//    public void heartDecrease() {
//
//        if(this.heartCount > 0){
//            this.heartCount -= 1;
//        }else {
//            this.heartCount = 0;
//        }
//    }

    // 게시글 삭제
    public void posterDelete() {
        this.isDelete = true;
    }
}
