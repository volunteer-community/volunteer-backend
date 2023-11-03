package com.maple.volunteer.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityDetailResponseDto {

    private Long categoryId;
    private String categoryType;
    private Long communityId;
    private String communityTitle;
    private Integer communityParticipant;
    private Integer communityMaxParticipant;
    private String communityAuthor;
    private String communityStatus;
    private String communityContent;
    private String communityLocation;

    @Builder
    public CommunityDetailResponseDto(Long categoryId, String categoryType, Long communityId, String communityTitle, Integer communityParticipant, Integer communityMaxParticipant, String communityAuthor, String communityStatus, String communityContent, String communityLocation) {
        this.categoryId = categoryId;
        this.categoryType = categoryType;
        this.communityId = communityId;
        this.communityTitle = communityTitle;
        this.communityParticipant = communityParticipant;
        this.communityMaxParticipant = communityMaxParticipant;
        this.communityAuthor = communityAuthor;
        this.communityStatus = communityStatus;
        this.communityContent = communityContent;
        this.communityLocation = communityLocation;
    }
}
