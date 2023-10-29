package com.maple.volunteer.domain.communityuser;

import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.domain.poster.Poster;
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
public class CommunityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 커뮤니티_유저 ID

    private Boolean isWithdraw; // 커뮤니티 탈퇴 유무

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @OneToMany(mappedBy = "communityUser")
    private List<Poster> posterList;

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
}
