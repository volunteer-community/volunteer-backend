package com.maple.volunteer.repository.community;

import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.dto.community.CommunityDetailResponseDto;
import com.maple.volunteer.dto.community.CommunityResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    // 커뮤니티 상세
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityDetailResponseDto(" +
            "c.id AS communityId, " +
            "c.title AS communityTitle," +
            "c.participant AS communityParticipant," +
            "c.author AS communityAuhtor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent) " +
            "FROM Community c " +
            "WHERE c.id = :communityId ")
    Optional<CommunityDetailResponseDto> findCommunityDetailByCommunityId(@Param("communityId") Long communityId);


    // 커뮤니티 전체
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath) " +
            "FROM Community c " +
            "LEFT JOIN c.communityImgList ci " +
            "WHERE ci.imageNum = 1 ")
    Page<CommunityResponseDto> findAllCommunityList(Pageable pageable);

    // 커뮤니티 카테고리 별 조회
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath) " +
            "FROM Community c " +
            "LEFT JOIN c.communityImgList ci " +
            "LEFT JOIN c.category cg " +
            "WHERE cg.type = :categoryType ")
    Page<CommunityResponseDto> findCommunityListByCategoryType(@Param("categoryType") String categoryType, Pageable pageable);


    // 커뮤니티 제목 검색 (keyword를 기준으로 앞쪽 뒤쪽에 글자가 붙은 정보를 가져옴)
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath) " +
            "FROM Community c " +
            "LEFT JOIN c.communityImgList ci " +
            "WHERE c.title Like %:keyword% ")
    Page<CommunityResponseDto> findCommunityListBySearchTitle(@Param("keyword") String keyword, Pageable pageable);

    // 커뮤니티 작성자 검색
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath) " +
            "FROM Community c " +
            "LEFT JOIN c.communityImgList ci " +
            "WHERE c.author Like %:keyword% ")
    Page<CommunityResponseDto> findCommunityListBySearchAuthor(@Param("keyword") String keyword, Pageable pageable);

}
