package com.maple.volunteer.repository.poster;

import com.maple.volunteer.domain.poster.Poster;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosterRepository extends JpaRepository<Poster, Long> {

//    @Query("SELECT p " +
//            "FROM Poster p " +
//            "LEFT JOIN p.communityUser cu " +
//            "LEFT JOIN cu.community c " +
//            "WHERE c.id = :communityId ")
//    List<Poster> findPosterList(@Param("communityId") Long communityId);

//    @Query("SELECT NEW com.maple.volunteer.dto.poster.PosterResponseDto(" +
//            "p.id AS posterId, "+
//            "p.title AS posterTitle, "+
//            "p.content AS posterContent, "+
//            "p.author AS posterAuthor, "+
//            "p.heartCount AS heartCount, "+
//            "pi.imagePath AS posterImgPath) "+
//            "FROM Poster p " +
//            "LEFT JOIN p.posterImgList pi "+
//            "LEFT JOIN p.communityUser cu " +
//            "LEFT JOIN cu.community c " +
//            "WHERE c.id = :communityId ")
//    Page<PosterResponseDto> findAllPosterList(@Param("communityId") Long communityId, PageRequest pageable);




}
