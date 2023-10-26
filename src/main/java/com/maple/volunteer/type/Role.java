package com.maple.volunteer.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "첫 소셜 로그인 유저"),
    USER("ROLE_USER", "일반 사용자"),
    HOST("ROLE_HOST", "커뮤니티 관리자"),
    ADMIN("ROLE_ADMIN", "전체 관리자");

    private final String key;
    private final String title;
}