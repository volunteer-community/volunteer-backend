package com.maple.volunteer.domain.user;

import com.maple.volunteer.domain.common.BaseTime;
import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.domain.login.Login;
import com.maple.volunteer.type.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 유저 ID

    private String email;   // 유저 이메일
    private String profileImg;  // 유저 프로필 이미지
    private String name;    // 유저 이름
    private String phoneNumber; // 유저 핸드폰 번호
    private String nickname; // 유저 닉네임

    @Enumerated(EnumType.STRING)
    private Role role;  // 유저 역할

    @OneToOne(mappedBy = "user")
    private Login login;

    @OneToMany(mappedBy = "user")
    private List<CommunityUser> communityUserList;

    @Builder
    public User(String email, String profileImg, String name, String phoneNumber, Role role,String nickname) {
        this.email = email;
        this.profileImg = profileImg;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.nickname = nickname;
    }


}
