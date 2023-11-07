package com.maple.volunteer.domain.posterimg;

import com.maple.volunteer.domain.poster.Poster;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class PosterImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 게시글 이미지 ID

    private String imagePath; // 게시글 이미지 URL
    private Integer imageNum;   // 게시글 이미지 번호
    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Poster poster;

    @Builder
    public PosterImg(String imagePath, Integer imageNum, Poster poster, boolean isDelete) {
        this.imagePath = imagePath;
        this.imageNum = imageNum;
        this.poster = poster;
        this.isDelete = isDelete;
    }
}
