package com.maple.volunteer.dto.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityHostResponseDto {

    private String hostNickName; // 커뮤니티 생성자 닉네임
    private String hostProfileImg; // 커뮤니티 생성자 프사
    private Long communityId; // 커뮤니티 ID
    private String communityTitle;

    @Builder
    public CommunityHostResponseDto(String hostNickName, String hostProfileImg, Long communityId, String communityTitle) {
        this.hostNickName = hostNickName;
        this.hostProfileImg = hostProfileImg;
        this.communityId = communityId;
        this.communityTitle = communityTitle;
    }
}

