package com.maple.volunteer.domain.communityimg;

import com.maple.volunteer.domain.community.Community;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class CommunityImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 커뮤니티 이미지 ID

    private String imagePath; // 커뮤니티 이미지 URL
    private Integer imageNum; // 커뮤니티 이미지 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Builder
    public CommunityImg(String imagePath, Integer imageNum, Community community) {
        this.imagePath = imagePath;
        this.imageNum = imageNum;
        this.community = community;
    }
}
