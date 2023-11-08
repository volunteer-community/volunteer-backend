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

    private String categoryType;
    private String communityTitle;
    private Integer communityMaxParticipant;
    private String communityContent;
    private String communityLocation;

    @Builder
    public CommunityRequestDto(String categoryType, String communityTitle, Integer communityMaxParticipant, String communityContent, String communityLocation) {
        this.categoryType = categoryType;
        this.communityTitle = communityTitle;
        this.communityMaxParticipant = communityMaxParticipant;
        this.communityContent = communityContent;
        this.communityLocation = communityLocation;
    }



}
