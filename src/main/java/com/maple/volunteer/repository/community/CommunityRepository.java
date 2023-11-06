package com.maple.volunteer.repository.community;

import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.dto.community.CommunityDetailResponseDto;
import com.maple.volunteer.dto.community.CommunityResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    // 커뮤니티 상세
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityDetailResponseDto(" +
            "cg.id AS categoryId, " +
            "cg.type AS categoryType, " +
            "c.id AS communityId, " +
            "c.title AS communityTitle," +
            "c.participant AS communityParticipant," +
            "c.maxParticipant AS communityMaxParticipant," +
            "c.author AS communityAuhtor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation, " +
            "c.createdAt AS communityCreatedAt, " +
            "c.updatedAt AS communityUpdatedAt) " +
            "FROM Community c " +
            "LEFT JOIN c.category cg " +
            "WHERE c.id = :communityId ")
    Optional<CommunityDetailResponseDto> findCommunityDetailByCommunityId(@Param("communityId") Long communityId);


    // 커뮤니티 전체
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "cg.id AS categoryId, " +
            "cg.type AS categoryType, " +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.maxParticipant AS communityMaxParticipant, " +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath, " +
            "c.createdAt AS communityCreatedAt, " +
            "c.updatedAt AS communityUpdatedAt) " +
            "FROM Community c " +
            "LEFT JOIN c.category cg " +
            "LEFT JOIN c.communityImgList ci " +
            "WHERE ci.imageNum = 1 AND c.isDelete = false ")
    Page<CommunityResponseDto> findAllCommunityList(Pageable pageable);

    // 커뮤니티 카테고리 별 조회
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "cg.id AS categoryId, " +
            "cg.type AS categoryType, " +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.maxParticipant AS communityMaxParticipant," +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath, " +
            "c.createdAt AS communityCreatedAt, " +
            "c.updatedAt AS communityUpdatedAt) " +
            "FROM Community c " +
            "LEFT JOIN c.communityImgList ci " +
            "LEFT JOIN c.category cg " +
            "WHERE cg.id = :categoryId AND ci.imageNum = 1 AND c.isDelete = false " )
    Page<CommunityResponseDto> findCommunityListByCategoryType(@Param("categoryId") Long categoryId, Pageable pageable);


    // 커뮤니티 제목 검색 (keyword를 기준으로 앞쪽 뒤쪽에 글자가 붙은 정보를 가져옴)
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "cg.id AS categoryId, " +
            "cg.type AS categoryType, " +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.maxParticipant AS communityMaxParticipant," +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath, " +
            "c.createdAt AS communityCreatedAt, " +
            "c.updatedAt AS communityUpdatedAt) " +
            "FROM Community c " +
            "LEFT JOIN c.category cg " +
            "LEFT JOIN c.communityImgList ci " +
            "WHERE c.title Like %:keyword% AND ci.imageNum = 1 AND c.isDelete = false ")
    Page<CommunityResponseDto> findCommunityListBySearchTitle(@Param("keyword") String keyword, Pageable pageable);

    // 커뮤니티 작성자 검색
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "cg.id AS categoryId, " +
            "cg.type AS categoryType, " +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.maxParticipant AS communityMaxParticipant, " +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath, " +
            "c.createdAt AS communityCreatedAt, " +
            "c.updatedAt AS communityUpdatedAt) " +
            "FROM Community c " +
            "LEFT JOIN c.category cg " +
            "LEFT JOIN c.communityImgList ci " +
            "WHERE c.author Like %:keyword% AND ci.imageNum = 1 AND c.isDelete = false ")
    Page<CommunityResponseDto> findCommunityListBySearchAuthor(@Param("keyword") String keyword, Pageable pageable);


    // 작성자로 커뮤니티 가져오기(내가 만든)
    @Query("SELECT NEW com.maple.volunteer.dto.community.CommunityResponseDto(" +
            "cg.id AS categoryId, " +
            "cg.type AS categoryType, " +
            "c.id AS communityId," +
            "c.title AS communityTitle, " +
            "c.participant AS communityParticipant, " +
            "c.maxParticipant AS communityMaxParticipant, " +
            "c.author AS communityAuthor," +
            "c.status AS communityStatus," +
            "c.content AS communityContent," +
            "c.location AS communityLocation," +
            "ci.imagePath AS communityMainImgPath, " +
            "c.createdAt AS communityCreatedAt, " +
            "c.updatedAt AS communityUpdatedAt) " +
            "FROM Community c " +
            "LEFT JOIN c.category cg " +
            "LEFT JOIN c.communityImgList ci " +
            "WHERE c.author = :author AND ci.imageNum = 1 AND c.isDelete = false ")
    Page<CommunityResponseDto> findCommunityListByAuthor(@Param("author") String author, Pageable pageable);

    // 커뮤니티 삭제
    @Query("UPDATE Community c " +
            "SET c.isDelete = :status " +
            "WHERE c.id = :communityId")
    @Modifying(clearAutomatically = true)
    void deleteCommunityId(@Param("communityId") Long communityId, @Param("status") Boolean status);

    // 참여 인원 증가
    @Query("UPDATE Community c " +
            "SET c.participant = c.participant + 1 " +
            "WHERE c.id = :communityId ")
    @Modifying(clearAutomatically = true)
    void participantIncrease(@Param("communityId") Long communityId);

    // 커뮤니티 id로 삭제 안된 것만 가져오기
    @Query("SELECT c " +
            "FROM Community c " +
            "WHERE c.id = :communityId AND c.isDelete = false ")
    Optional<Community> findCommunityByFalse(@Param("communityId") Long communityId);
}
