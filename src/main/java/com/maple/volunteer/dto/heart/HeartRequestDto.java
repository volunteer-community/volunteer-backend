package com.maple.volunteer.dto.heart;

import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HeartRequestDto {

    private Long userId;
    private Long posterId;

    @Builder
    public HeartRequestDto(Long userId, Long posterId){
        this.userId = userId;
        this.posterId = posterId;
    }


    //좋아요 처음 생성
    public Heart toAddHeartEntity(User user, Poster poster){
        return Heart.builder()
                .user(user)
                .poster(poster)
                .status(true)
                .build();
    }

}
