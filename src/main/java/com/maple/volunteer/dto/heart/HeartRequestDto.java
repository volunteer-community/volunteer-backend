package com.maple.volunteer.dto.heart;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HeartRequestDto {

    private Long communityUserId;
    private Long posterId;

    @Builder
    public HeartRequestDto(Long communityUserId, Long posterId){
        this.communityUserId = communityUserId;
        this.posterId = posterId;
    }


    //좋아요 처음 생성
    public Heart toAddHeartEntity(CommunityUser communityUser, Poster poster){
        return Heart.builder()
                .communityUser(communityUser)
                .poster(poster)
                .status(true)
                .build();
    }

}
