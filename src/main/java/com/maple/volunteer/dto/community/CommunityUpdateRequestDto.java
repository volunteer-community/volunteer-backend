package com.maple.volunteer.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityUpdateRequestDto {

    private String communityContent;
    private Integer communityMaxParticipant;

    @Builder
    public CommunityUpdateRequestDto(String communityContent, Integer communityMaxParticipant) {
        this.communityContent = communityContent;
        this.communityMaxParticipant = communityMaxParticipant;
    }
}
