package com.maple.volunteer.type;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommunityStatus {

    COMMUNITY_RECRUITMENT_ING("모집 중"),
    COMMUNITY_RECRUITMENT_END("모집 마감");

    private final String description;
}
