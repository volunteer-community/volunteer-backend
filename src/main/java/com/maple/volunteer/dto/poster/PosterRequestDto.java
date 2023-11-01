package com.maple.volunteer.dto.poster;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.poster.Poster;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PosterRequestDto {

    private String posterTitle;
    private String posterContent;


    @Builder
    public PosterRequestDto(String posterTitle, String posterContent) {
        this.posterTitle = posterTitle;
        this.posterContent = posterContent;
    }

//    public Poster toEntity(CommunityUser communityUser){
//        return Poster.builder()
//                .title(posterTitle)
//                .content(posterContent)
//                .author()
//                .heartCount(0)
//                .isDelete(false)
//                // 자동으로 매핑이 들어갈거임
//                .communityUser(communityUser)
//                .build();
//    }
}
