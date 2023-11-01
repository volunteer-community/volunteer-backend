package com.maple.volunteer.repository.posterimg;

import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.posterimg.PosterImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PosterImgRepository extends JpaRepository<PosterImg, Long> {

    PosterImg findByPoster(Poster poster);

    @Modifying
    @Query("DELETE FROM PosterImg pi WHERE pi.poster = ?1")
    void deleteAllByPoster(Poster poster);

}
