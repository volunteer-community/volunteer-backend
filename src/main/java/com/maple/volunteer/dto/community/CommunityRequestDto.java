package com.maple.volunteer.dto.community;

import com.maple.volunteer.domain.category.Category;
import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.type.CommunityStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityRequestDto {

    private String communityTitle;
    private Integer communityMaxParticipant;
    private String communityContent;
    private String communityLocation;

    @Builder
    public CommunityRequestDto(String communityTitle, Integer communityMaxParticipant, String communityContent, String communityLocation) {
        this.communityTitle = communityTitle;
        this.communityMaxParticipant = communityMaxParticipant;
        this.communityContent = communityContent;
        this.communityLocation = communityLocation;
    }



}
