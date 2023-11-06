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
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Community extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 커뮤니티 ID

    @Column(length = 30)
    @Size(min = 1, max = 30)
    private String title;   // 커뮤니티 제목

    private Integer participant;    // 커뮤니티 참여 인원

    private Integer maxParticipant; // 커뮤니티 모집 인원
    private String author;  // 커뮤니티 작성자

    @Column(length = 5)
    @Size(max = 5)
    private String status;  // 커뮤니티 활동 상태

    @Lob
    private String content; // 커뮤니티 내용

    @Column(length = 30)
    @Size(max = 30)
    private String location;   // 커뮤니티 지역명
    private Boolean isDelete;   // 커뮤니티 삭제 유무

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
    public void communityUpdate(Category category, String title, Integer participant, Integer maxParticipant, String author, String status, String content, String location) {
        this.category = category;
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
