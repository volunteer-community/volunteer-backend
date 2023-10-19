package com.maple.volunteer.domain.community;

import com.maple.volunteer.domain.category.Category;
import com.maple.volunteer.domain.common.BaseTime;
import com.maple.volunteer.domain.communityimg.CommunityImg;
import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.user.User;
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
public class Community extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 커뮤니티 ID

    private String title;   // 커뮤니티 제목
    private Integer participant;    // 커뮤니티 참여 인원
    private String author;  // 커뮤니티 작성자
    private String status;  // 커뮤니티 활동 상태
    private String content; // 커뮤니티 내용
    private String location;   // 커뮤니티 한줄 소개

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "community")
    private List<CommunityUser> communityUserList;

    @OneToMany(mappedBy = "community")
    private List<CommunityImg> communityImgList;

    @Builder
    public Community(String title, Integer participant, String author, String status, String content, String location, Category category) {
        this.title = title;
        this.participant = participant;
        this.author = author;
        this.status = status;
        this.content = content;
        this.location = location;
        this.category = category;
    }
}
