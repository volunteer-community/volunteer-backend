package com.maple.volunteer.repository.poster;

import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.dto.poster.PosterDetailResponseDto;
import com.maple.volunteer.dto.poster.PosterResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PosterRepository extends JpaRepository<Poster, Long> {

    @Query("SELECT NEW com.maple.volunteer.dto.poster.PosterResponseDto(" +
            "p.id AS posterId, "+
            "p.title AS posterTitle, "+
            "p.content AS posterContent, "+
            "p.author AS posterAuthor, "+
            "p.heartCount AS heartCount, "+
            "pi.imagePath AS posterImgPath) "+
            "FROM Poster p " +
            "LEFT JOIN p.posterImgList pi "+
            "LEFT JOIN p.communityUser cu " +
            "LEFT JOIN cu.community c " +
            "WHERE c.id = :communityId ")
    Page<PosterResponseDto> findAllPosterList(@Param("communityId") Long communityId, Pageable pageable);



    @Query("SELECT NEW com.maple.volunteer.dto.poster.PosterDetailResponseDto(" +
            "p.id AS posterId, "+
            "p.title AS posterTitle, "+
            "p.author AS posterAuthor, "+
            "p.content AS posterContent, "+
            "p.heartCount AS heartCount, "+
            "pi.imagePath AS posterImgPath) "+
            "FROM Poster p " +
            "LEFT JOIN p.posterImgList pi "+
            "LEFT JOIN p.communityUser cu " +
            "LEFT JOIN cu.community c " +
            "WHERE c.id = :communityId AND p.id = :posterId")
    Optional<PosterDetailResponseDto> findPosterDetailByCommunityIdAndPosterId(@Param("communityId") Long communityId, @Param("posterId") Long posterId);
}
