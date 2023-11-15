package com.maple.volunteer.domain.communityuser;

import com.maple.volunteer.domain.comment.Comment;
import com.maple.volunteer.domain.common.BaseTime;
import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class CommunityUser extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 커뮤니티_유저 ID

    @Column(nullable = false)
    private Boolean isWithdraw; // 커뮤니티 탈퇴 유무

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @OneToMany(mappedBy = "communityUser")
    private List<Poster> posterList;

    @OneToMany(mappedBy = "communityUser")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "communityUser")
    private List<Heart> heartList;

    @Builder
    public CommunityUser(User user, Boolean isWithdraw, Community community) {
        this.user = user;
        this.isWithdraw = isWithdraw;
        this.community = community;
    }

    // 커뮤니티 탈퇴
    public void communityWithdraw() {
        this.isWithdraw = true;
    }

    // 커뮤니티 재가입
    public void communityReSign() {
        this.isWithdraw = false;
    }
}
