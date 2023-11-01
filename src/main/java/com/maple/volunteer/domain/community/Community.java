package com.maple.volunteer.domain.community;

import com.maple.volunteer.domain.category.Category;
import com.maple.volunteer.domain.common.BaseTime;
import com.maple.volunteer.domain.communityimg.CommunityImg;
import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.type.CommunityStatus;
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
    private Integer maxParticipant; // 커뮤니티 모집 인원
    private String author;  // 커뮤니티 작성자
    private String status;  // 커뮤니티 활동 상태
    private String content; // 커뮤니티 내용
    private String location;   // 커뮤니티 한줄 소개
    private Boolean isDelete;   // 커뮤니티 삭제 유무

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "community")
    private List<CommunityUser> communityUserList;

    @OneToMany(mappedBy = "community")
    private List<CommunityImg> communityImgList;

    @Builder
    public Community(String title, Integer participant, Integer maxParticipant, String author, String status, String content, String location, boolean isDelete, Category category) {
        this.title = title;
        this.participant = participant;
        this.maxParticipant = maxParticipant;
        this.author = author;
        this.status = status;
        this.content = content;
        this.location = location;
        this.isDelete = isDelete;
        this.category = category;
    }

    // 커뮤니티 수정
    public void communityUpdate(String title, Integer participant, Integer maxParticipant, String author, String status, String content, String location) {
        this.title = title;
        this.participant = participant;
        this.maxParticipant = maxParticipant;
        this.author = author;
        this.status = status;
        this.content = content;
        this.location = location;
    }


    // 커뮤니티 참여 인원 증가
    public void communityParticipantIncrease() {
        this.participant += 1;
    }

    // 커뮤니티 참여 인원 감소
    public void communityParticipantDecrease() {
        this.participant -= 1;
    }

    // 커뮤니티 상태 변경
    public void communityRecruitmentEnd() {
        this.status = CommunityStatus.COMMUNITY_RECRUITMENT_END.getDescription();
    }

    public void communityRecruitmentIng() {
        this.status = CommunityStatus.COMMUNITY_RECRUITMENT_ING.getDescription();
    }

    // 커뮤니티 삭제
    public void communityDelete() {
        this.isDelete = true;
    }
}
