package com.maple.volunteer.repository.posterimg;

import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.posterimg.PosterImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PosterImgRepository extends JpaRepository<PosterImg, Long> {

    PosterImg findByPoster(Poster poster);

    // 게시글 이미지 수정
    @Modifying
    @Query("DELETE FROM PosterImg pi WHERE pi.poster = ?1")
    void deleteAllByPoster(Poster poster);

    // 게시글 삭제 시 이미지
    @Query("UPDATE PosterImg pi " +
            "SET pi.isDelete = :status " +
            "WHERE pi.id = :posterImgId ")
    @Modifying(clearAutomatically = true)
    void deleteByPosterImgId(@Param("posterImgId") Long posterImgId ,@Param("status") Boolean status);

    @Query("SELECT pi " +
            "FROM PosterImg  pi " +
            "LEFT JOIN pi.poster p " +
            "LEFT JOIN p.communityUser cu " +
            "LEFT JOIN cu.community c " +
            "WHERE c.id = :communityId " )
    PosterImg findByPosterByCommunityId(@Param("communityId") Long communityId);

    @Query("SELECT pi " +
            "FROM PosterImg pi " +
            "LEFT JOIN pi.poster p " +
            "WHERE p.id = :posterId ")
    PosterImg findByPosterId(@Param("posterId") Long posterId);
}
