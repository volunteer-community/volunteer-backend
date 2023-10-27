package com.maple.volunteer.service.community;

import com.maple.volunteer.domain.category.Category;
import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.domain.communityimg.CommunityImg;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.dto.community.*;
import com.maple.volunteer.exception.CommunityRecruitmentException;
import com.maple.volunteer.exception.CommunityUpdateException;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.category.CategoryRepository;
import com.maple.volunteer.repository.community.CommunityRepository;
import com.maple.volunteer.repository.communityimg.CommunityImgRepository;
import com.maple.volunteer.repository.communityuser.CommunityUserRepository;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.service.s3upload.S3UploadService;
import com.maple.volunteer.type.CommunityStatus;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final CategoryRepository categoryRepository;
    private final CommunityRepository communityRepository;
    private final CommunityUserRepository communityUserRepository;
    private final CommunityImgRepository communityImgRepository;
    private final S3UploadService s3UploadService;
    private final CommonService commonService;

    // 커뮤니티 생성
    @Transactional
    public CommonResponseDto<Object> communityCreate(String categoryType, List<MultipartFile> multipartFileList, CommunityRequestDto communityRequestDto) {


        Category category = categoryRepository.findByCategoryType(categoryType)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_TYPE_NOT_FOUND));


    // 토큰 값으로 Author 추가 필요
        Community community = Community.builder()
                .title(communityRequestDto.getCommunityTitle())
                .participant(0)
                .maxParticipant(communityRequestDto.getCommunityMaxParticipant())
                .content(communityRequestDto.getCommunityContent())
                .status(CommunityStatus.COMMUNITY_RECRUITMENT_ING.getDescription())
                .location(communityRequestDto.getCommunityLocation())
                .category(category)
                .build();

        communityRepository.save(community);

        createCommunityImage(multipartFileList, community);


        return commonService.successResponse(SuccessCode.COMMUNITY_CREATE_SUCCESS.getDescription(), HttpStatus.CREATED, null);

    }

    // 모든 커뮤니티 조회 (페이지 네이션)
    public CommonResponseDto<Object> allCommunityInquiry(int page, int size, String sortBy) {

        PageRequest pageable = PageRequest.of(page -1, size, Sort.by(sortBy).descending());

        Page<CommunityResponseDto> data = communityRepository.findAllCommunityList(pageable);

        List<CommunityResponseDto> allCommunityList = data.getContent();

        PaginationDto paginationDto = PaginationDto.builder()
                .totalPages(data.getTotalPages())
                .totalElements(data.getTotalElements())
                .pageNo(data.getNumber())
                .isLastPage(data.isLast())
                .build();

        CommunityListResponseDto allCommunityListResponseDto = CommunityListResponseDto.builder()
                .communityList(allCommunityList)
                .paginationDto(paginationDto)
                .build();

        return commonService.successResponse(SuccessCode.ALL_COMMUNITY_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, allCommunityListResponseDto);
    }

    // 커뮤니티 카테고리 별 조회 (페이지 네이션)
    public CommonResponseDto<Object> categoryCommunityInquiry(Long categoryId, int page, int size, String sortBy) {

        PageRequest pageable = PageRequest.of(page -1 , size, Sort.by(sortBy).descending());

        Page<CommunityResponseDto> data = communityRepository.findCommunityListByCategoryType(categoryId, pageable);

        List<CommunityResponseDto> categoryCommunityList = data.getContent();

        PaginationDto paginationDto = PaginationDto.builder()
                .totalPages(data.getTotalPages())
                .totalElements(data.getTotalElements())
                .pageNo(data.getNumber())
                .isLastPage(data.isLast())
                .build();

        CommunityListResponseDto categoryCommunityListResponseDto = CommunityListResponseDto.builder()
                .communityList(categoryCommunityList)
                .paginationDto(paginationDto)
                .build();

        return commonService.successResponse(SuccessCode.CATEGORY_COMMUNITY_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, categoryCommunityListResponseDto);
    }

    // 커뮤니티 리스트 검색 (페이지 네이션) -> 커뮤니티 제목
    public CommonResponseDto<Object> searchTitleCommunityInquiry(String keyword , int page, int size, String sortBy) {

        PageRequest pageable = PageRequest.of(page -1 , size, Sort.by(sortBy).descending());

        Page<CommunityResponseDto> data = communityRepository.findCommunityListBySearchTitle(keyword, pageable);

        List<CommunityResponseDto> searchTitleCommunityList = data.getContent();

        PaginationDto paginationDto = PaginationDto.builder()
                .totalPages(data.getTotalPages())
                .totalElements(data.getTotalElements())
                .pageNo(data.getNumber())
                .isLastPage(data.isLast())
                .build();

        CommunityListResponseDto searchTitleCommunityListResponseDto = CommunityListResponseDto.builder()
                .communityList(searchTitleCommunityList)
                .paginationDto(paginationDto)
                .build();

        return commonService.successResponse(SuccessCode.SEARCH_COMMUNITY_TITLE_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, searchTitleCommunityListResponseDto);
    }

    // 커뮤니티 리스트 검색 (페이지 네이션) -> 커뮤니티 작성자
    public CommonResponseDto<Object> searchAuthorCommunityInquiry(String keyword , int page, int size, String sortBy) {

        PageRequest pageable = PageRequest.of(page -1 , size, Sort.by(sortBy).descending());

        Page<CommunityResponseDto> data = communityRepository.findCommunityListBySearchAuthor(keyword, pageable);

        List<CommunityResponseDto> searchAuthorCommunityList = data.getContent();

        PaginationDto paginationDto = PaginationDto.builder()
                .totalPages(data.getTotalPages())
                .totalElements(data.getTotalElements())
                .pageNo(data.getNumber())
                .isLastPage(data.isLast())
                .build();

        CommunityListResponseDto searchAuthorCommunityListResponseDto = CommunityListResponseDto.builder()
                .communityList(searchAuthorCommunityList)
                .paginationDto(paginationDto)
                .build();

        return commonService.successResponse(SuccessCode.SEARCH_COMMUNITY_AUTHOR_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, searchAuthorCommunityListResponseDto);
    }

    // 커뮤니티 상세 조회
    public CommonResponseDto<Object> communityDetailInquiry(Long communityId) {

        CommunityDetailResponseDto communityDetailResponseDto = communityRepository.findCommunityDetailByCommunityId(communityId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        List<CommunityImgResponseDto> communityImgResponseDtoList = communityImgRepository.findCommunityImgListByCommunityId(communityId);

        CommunityDetailAndImgResponseDto communityDetailAndImgResponseDto = CommunityDetailAndImgResponseDto.builder()
                .communityDetail(communityDetailResponseDto)
                .communityImgPathList(communityImgResponseDtoList)
                .build();

        return commonService.successResponse(SuccessCode.COMMUNITY_DETAIL_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, communityDetailAndImgResponseDto);
    }

    // 커뮤니티 수정 (추후 로직 수정)
    @Transactional
    public CommonResponseDto<Object> communityUpdate(Long communityId, List<MultipartFile> multipartFileList, CommunityRequestDto communityRequestDto) {


        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        communityImgRepository.deleteByCommunityId(communityId);

        if (community.getParticipant() > communityRequestDto.getCommunityMaxParticipant()) {  // 참여 인원보다 작을 때
            throw new CommunityUpdateException(ErrorCode.MAX_PARTICIPANT_LOW_ERROR);
        }

        if (community.getParticipant().equals(communityRequestDto.getCommunityMaxParticipant())) {    // 참여 인원과 같을 때
            community.communityRecruitmentEnd();
        }

        if (community.getStatus().equals(CommunityStatus.COMMUNITY_RECRUITMENT_END.getDescription())) { // 상태가 모집 마감일 때
            community.communityRecruitmentIng();
        }

        community.communityUpdate(communityRequestDto.getCommunityTitle(), community.getParticipant(),
                communityRequestDto.getCommunityMaxParticipant(), community.getAuthor(),
                community.getStatus(), communityRequestDto.getCommunityContent(), communityRequestDto.getCommunityLocation());

        createCommunityImage(multipartFileList, community);

        return commonService.successResponse(SuccessCode.COMMUNITY_UPDATE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 커뮤니티 참가
    // 유저 생성이 되면 유저를 넣어서 저장
    @Transactional
    public CommonResponseDto<Object> communitySignup(Long communityId) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        if (community.getStatus().equals(CommunityStatus.COMMUNITY_RECRUITMENT_END.getDescription())) {
            throw new CommunityRecruitmentException(ErrorCode.COMMUNITY_RECRUITMENT_END_ERROR);
        }

        community.communityParticipantIncrease();

        if (community.getParticipant().equals(community.getMaxParticipant())) {
            community.communityRecruitmentEnd();
        }

        return commonService.successResponse(SuccessCode.COMMUNITY_SIGNUP.getDescription(), HttpStatus.CREATED, null);
    }

    // 커뮤니티 탈퇴
    // 커뮤니티 삭제


    // 이미지 저장
    private void createCommunityImage(List<MultipartFile> multipartFileList, Community community) {

        List<String> communityImageUrlList = s3UploadService.communityUpload(multipartFileList);

        Integer imgNum = 1;

        for (String imgPath : communityImageUrlList) {
            CommunityImg communityImg = CommunityImg.builder()
                    .imagePath(imgPath)
                    .imageNum(imgNum)
                    .community(community)
                    .build();

            communityImgRepository.save(communityImg);

            imgNum++;
        }
    }
}
