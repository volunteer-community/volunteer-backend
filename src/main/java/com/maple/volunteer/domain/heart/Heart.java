package com.maple.volunteer.domain.heart;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 좋아요 ID

    private Boolean status; // 좋아요 상태 true +1 / false -1 // true ->false 변환과정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_user_id")
    private CommunityUser communityUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Poster poster;

    @Builder
    public Heart(CommunityUser communityUser, Poster poster, Boolean status){
        this.status = status;
        this.communityUser = communityUser;
        this.poster = poster;
    }

    // 좋아요 상태 변경
    public void updateHeartStatus() {
        this.status = false;
    }
}
