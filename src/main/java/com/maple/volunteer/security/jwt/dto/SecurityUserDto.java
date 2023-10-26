package com.maple.volunteer.security.jwt.dto;

import com.maple.volunteer.type.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityUserDto {
    private String email;
    private String nickname;
    private String profileImage;
    private Role role;
    private Long id;
}
