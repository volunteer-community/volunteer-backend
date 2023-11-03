package com.maple.volunteer.domain.login;

import com.maple.volunteer.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 로그인 ID
    private String refreshToken;    // 리프레쉬 토큰
    private String provider;    // 소셜 로그인 제공자

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Login(String refreshToken, String provider, User user) {
        this.refreshToken = refreshToken;
        this.provider = provider;
        this.user = user;
    }

    // 리프레쉬 토큰 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
