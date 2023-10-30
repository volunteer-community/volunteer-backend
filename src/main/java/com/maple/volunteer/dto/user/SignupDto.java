package com.maple.volunteer.dto.user;

import com.maple.volunteer.type.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupDto {
    private String email;
    private String name;
    private String provider;
    private String nickname;
    private String phoneNumber;
    private Role role;
}
