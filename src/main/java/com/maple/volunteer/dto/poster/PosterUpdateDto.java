package com.maple.volunteer.dto.poster;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.poster.Poster;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PosterUpdateDto {

    private String posterTitle;
    private String posterContent;


    @Builder
    public PosterUpdateDto(String posterTitle, String posterContent, String posterAuthor) {
        this.posterTitle = posterTitle;
        this.posterContent = posterContent;
    }

//    public Poster toEntity(){
//        return Poster.builder()
//                .title(posterTitle)
//                .content(posterContent)
//                .build();
//    }

}
