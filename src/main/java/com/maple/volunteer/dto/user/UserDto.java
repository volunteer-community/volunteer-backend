package com.maple.volunteer.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {

    private String email;
    private String name;
    private String profileImg;
    private String nickname;
    private String phoneNumber;
    private boolean isDeleted;

    @Builder
    public UserDto(String email, String name, String profileImg, String nickname, String phoneNumber, boolean isDeleted) {
        this.email = email;
        this.name = name;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.isDeleted = isDeleted;
    }
}
