package com.maple.volunteer.dto.user;

import com.maple.volunteer.type.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewUserDto {
    private String email;
    private String picture;
    private String provider;
    private String role;
    private String name;
}
