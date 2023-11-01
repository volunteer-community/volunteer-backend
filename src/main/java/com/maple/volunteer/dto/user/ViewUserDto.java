package com.maple.volunteer.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViewUserDto {
    private String name;
    private String email;
    private String picture;
    private String nickname;
    private String phone;
}
