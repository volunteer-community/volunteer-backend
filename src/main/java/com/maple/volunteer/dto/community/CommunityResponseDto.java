package com.maple.volunteer.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityResponseDto {

    private Long communityId;
    private String communityTitle;
    private Integer communityParticipant;
    private String communityAuthor;
    private String communityStatus;
    private String communityContent;
    private String communityLocation;
    private String communityMainImgPath;

    @Builder
    public CommunityResponseDto(Long communityId, String communityTitle, Integer communityParticipant, String communityAuthor, String communityStatus, String communityContent, String communityLocation, String communityMainImgPath) {
        this.communityId = communityId;
        this.communityTitle = communityTitle;
        this.communityParticipant = communityParticipant;
        this.communityAuthor = communityAuthor;
        this.communityStatus = communityStatus;
        this.communityContent = communityContent;
        this.communityLocation = communityLocation;
        this.communityMainImgPath = communityMainImgPath;
    }
}
