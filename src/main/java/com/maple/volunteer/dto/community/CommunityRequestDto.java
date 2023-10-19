package com.maple.volunteer.dto.community;

import com.maple.volunteer.domain.category.Category;
import com.maple.volunteer.domain.community.Community;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityRequestDto {

    private String communityTitle;
    private Integer communityParticipant;
    private String communityAuthor;
    private String communityStatus;
    private String communityContent;
    private String communityLocation;

    @Builder
    public CommunityRequestDto(String communityTitle, Integer communityParticipant, String communityAuthor, String communityStatus, String communityContent, String communityLocation) {
        this.communityTitle = communityTitle;
        this.communityParticipant = communityParticipant;
        this.communityAuthor = communityAuthor;
        this.communityStatus = communityStatus;
        this.communityContent = communityContent;
        this.communityLocation = communityLocation;
    }


    public Community toEntity(Category category) {
        return Community.builder()
                .title(communityTitle)
                .participant(communityParticipant)
                .author(communityAuthor)
                .content(communityContent)
                .location(communityLocation)
                .category(category)
                .build();
    }
}
