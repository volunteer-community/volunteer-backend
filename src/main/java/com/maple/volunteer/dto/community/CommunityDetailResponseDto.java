package com.maple.volunteer.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityDetailResponseDto {

    private Long communityId;
    private String communityTitle;
    private Integer communityParticipant;
    private String communityAuthor;
    private String communityStatus;
    private String communityContent;

    @Builder
    public CommunityDetailResponseDto(Long communityId, String communityTitle, Integer communityParticipant, String communityAuthor, String communityStatus, String communityContent) {
        this.communityId = communityId;
        this.communityTitle = communityTitle;
        this.communityParticipant = communityParticipant;
        this.communityAuthor = communityAuthor;
        this.communityStatus = communityStatus;
        this.communityContent = communityContent;
    }
}