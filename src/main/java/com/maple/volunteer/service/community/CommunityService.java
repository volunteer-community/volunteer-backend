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

        // 카테고리 가져오기
        Category category = categoryRepository.findByCategoryType(categoryType)
                // 카테고리 값 없으면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_TYPE_NOT_FOUND));


        // 토큰 값으로 Author 추가 필요
        // Author 값을 따로 넣어주기 위해 따로
        Community community = Community.builder()
                .title(communityRequestDto.getCommunityTitle())
                .participant(0)
                .maxParticipant(communityRequestDto.getCommunityMaxParticipant())
                .content(communityRequestDto.getCommunityContent())
                .status(CommunityStatus.COMMUNITY_RECRUITMENT_ING.getDescription())
                .location(communityRequestDto.getCommunityLocation())
                .isDelete(false)
                .category(category)
                .build();

        // 커뮤니티 저장
        communityRepository.save(community);

        // S3에 이미지 저장
        createCommunityImage(multipartFileList, community);


        return commonService.successResponse(SuccessCode.COMMUNITY_CREATE_SUCCESS.getDescription(), HttpStatus.CREATED, null);

    }

    // 모든 커뮤니티 조회 (페이지 네이션)
    public CommonResponseDto<Object> allCommunityInquiry(int page, int size, String sortBy) {

        // 페이지, 요소 개수, 정렬 설정
        PageRequest pageable = PageRequest.of(page -1, size, Sort.by(sortBy).descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityRepository.findAllCommunityList(pageable);

        // 커뮤니티 리스트 가져오기
        List<CommunityResponseDto> allCommunityList = data.getContent();

        // 페이지네이션 설정
        PaginationDto paginationDto = PaginationDto.builder()
                .totalPages(data.getTotalPages())
                .totalElements(data.getTotalElements())
                .pageNo(data.getNumber())
                .isLastPage(data.isLast())
                .build();

        // 페이지네이션을 포함한 커뮤니티 리스트 반환
        CommunityListResponseDto allCommunityListResponseDto = CommunityListResponseDto.builder()
                .communityList(allCommunityList)
                .paginationDto(paginationDto)
                .build();

        return commonService.successResponse(SuccessCode.ALL_COMMUNITY_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, allCommunityListResponseDto);
    }

    // 커뮤니티 카테고리 별 조회 (페이지 네이션)
    public CommonResponseDto<Object> categoryCommunityInquiry(Long categoryId, int page, int size, String sortBy) {

        // 페이지, 요소 개수, 정렬 설정
        PageRequest pageable = PageRequest.of(page -1 , size, Sort.by(sortBy).descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityRepository.findCommunityListByCategoryType(categoryId, pageable);

        // 커뮤니티 리스트 가져오기
        List<CommunityResponseDto> categoryCommunityList = data.getContent();

        // 페이지네이션 설정
        PaginationDto paginationDto = PaginationDto.builder()
                .totalPages(data.getTotalPages())
                .totalElements(data.getTotalElements())
                .pageNo(data.getNumber())
                .isLastPage(data.isLast())
                .build();

        // 페이지네이션을 포함한 커뮤니티 리스트 반환
        CommunityListResponseDto categoryCommunityListResponseDto = CommunityListResponseDto.builder()
                .communityList(categoryCommunityList)
                .paginationDto(paginationDto)
                .build();

        return commonService.successResponse(SuccessCode.CATEGORY_COMMUNITY_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, categoryCommunityListResponseDto);
    }

    // 커뮤니티 리스트 검색 (페이지 네이션) -> 커뮤니티 제목
    public CommonResponseDto<Object> searchTitleCommunityInquiry(String keyword , int page, int size, String sortBy) {

        // 페이지, 요소 개수, 정렬 설정
        PageRequest pageable = PageRequest.of(page -1 , size, Sort.by(sortBy).descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityRepository.findCommunityListBySearchTitle(keyword, pageable);

        // 커뮤니티 리스트 가져오기
        List<CommunityResponseDto> searchTitleCommunityList = data.getContent();

        // 페이지네이션 설정
        PaginationDto paginationDto = PaginationDto.builder()
                .totalPages(data.getTotalPages())
                .totalElements(data.getTotalElements())
                .pageNo(data.getNumber())
                .isLastPage(data.isLast())
                .build();

        // 페이지네이션을 포함한 커뮤니티 리스트 반환
        CommunityListResponseDto searchTitleCommunityListResponseDto = CommunityListResponseDto.builder()
                .communityList(searchTitleCommunityList)
                .paginationDto(paginationDto)
                .build();

        return commonService.successResponse(SuccessCode.SEARCH_COMMUNITY_TITLE_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, searchTitleCommunityListResponseDto);
    }

    // 커뮤니티 리스트 검색 (페이지 네이션) -> 커뮤니티 작성자
    public CommonResponseDto<Object> searchAuthorCommunityInquiry(String keyword , int page, int size, String sortBy) {

        // 페이지, 요소 개수, 정렬 설정
        PageRequest pageable = PageRequest.of(page -1 , size, Sort.by(sortBy).descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityRepository.findCommunityListBySearchAuthor(keyword, pageable);

        // 커뮤니티 리스트 가져오기
        List<CommunityResponseDto> searchAuthorCommunityList = data.getContent();

        // 페이지네이션 설정
        PaginationDto paginationDto = PaginationDto.builder()
                .totalPages(data.getTotalPages())
                .totalElements(data.getTotalElements())
                .pageNo(data.getNumber())
                .isLastPage(data.isLast())
                .build();

        // 페이지네이션을 포함한 커뮤니티 리스트 반환
        CommunityListResponseDto searchAuthorCommunityListResponseDto = CommunityListResponseDto.builder()
                .communityList(searchAuthorCommunityList)
                .paginationDto(paginationDto)
                .build();

        return commonService.successResponse(SuccessCode.SEARCH_COMMUNITY_AUTHOR_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, searchAuthorCommunityListResponseDto);
    }

    // 커뮤니티 상세 조회
    public CommonResponseDto<Object> communityDetailInquiry(Long communityId) {

        // 이미지를 제외한 커뮤니티 정보 가져오기
        CommunityDetailResponseDto communityDetailResponseDto = communityRepository.findCommunityDetailByCommunityId(communityId)
                // 값이 없다면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        // 커뮤니티 ID에 해당하는 이미지 가져오기
        List<CommunityImgResponseDto> communityImgResponseDtoList = communityImgRepository.findCommunityImgListByCommunityId(communityId);

        // 하나의 Dto로 커뮤니티 정보와 이미지 반환
        CommunityDetailAndImgResponseDto communityDetailAndImgResponseDto = CommunityDetailAndImgResponseDto.builder()
                .communityDetail(communityDetailResponseDto)
                .communityImgPathList(communityImgResponseDtoList)
                .build();

        return commonService.successResponse(SuccessCode.COMMUNITY_DETAIL_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, communityDetailAndImgResponseDto);
    }

    // 커뮤니티 수정 (추후 로직 수정)
    @Transactional
    public CommonResponseDto<Object> communityUpdate(Long communityId, List<MultipartFile> multipartFileList, CommunityRequestDto communityRequestDto) {

        // 커뮤니티 가져오기
        Community community = communityRepository.findById(communityId)
                // 커뮤니티가 없으면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        // 이미지 url 값만 가져오기
        List<CommunityImgPathDto> communityImgPathList = communityImgRepository.findCommunityImgPathList(communityId);

        // url 값 삭제
        for (CommunityImgPathDto communityImgPathDto : communityImgPathList) {
            String imgPath = communityImgPathDto.getCommunityImgPath();

            // s3 이미지 삭제
            s3UploadService.deleteFile(imgPath);
        }

        // db에 url 삭제
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

        // 받아온 데이터 업데이트
        community.communityUpdate(communityRequestDto.getCommunityTitle(), community.getParticipant(),
                communityRequestDto.getCommunityMaxParticipant(), community.getAuthor(),
                community.getStatus(), communityRequestDto.getCommunityContent(), communityRequestDto.getCommunityLocation());

        // 이미지 새로 업로드
        createCommunityImage(multipartFileList, community);

        return commonService.successResponse(SuccessCode.COMMUNITY_UPDATE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 커뮤니티 삭제
    @Transactional
    public CommonResponseDto<Object> communityDelete(Long communityId) {

        // 커뮤니티 가져오기
        Community community = communityRepository.findById(communityId)
                // 커뮤니티가 없다면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        // isDelete 값을 true로 변경
        community.communityDelete();

        return commonService.successResponse(SuccessCode.COMMUNITY_DELETE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 커뮤니티 참가
    // 유저 생성이 되면 유저를 넣어서 저장
    @Transactional
    public CommonResponseDto<Object> communitySignup(Long communityId) {

        // 커뮤니티 가져오기
        Community community = communityRepository.findById(communityId)
                // 커뮤니티가 없다면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        // 현재 상태가 모집 마감이면 오류 반환
        if (community.getStatus().equals(CommunityStatus.COMMUNITY_RECRUITMENT_END.getDescription())) {
            throw new CommunityRecruitmentException(ErrorCode.COMMUNITY_RECRUITMENT_END_ERROR);
        }

        // 참가 인원 증가
        community.communityParticipantIncrease();

        // 참가 인원 증가 후 모집 인원과 동일해지면 모집 마감으로 변경
        if (community.getParticipant().equals(community.getMaxParticipant())) {
            community.communityRecruitmentEnd();
        }

        return commonService.successResponse(SuccessCode.COMMUNITY_SIGNUP.getDescription(), HttpStatus.CREATED, null);
    }

    // 커뮤니티 탈퇴
    @Transactional
    public CommonResponseDto<Object> communityWithdraw(Long communityId) {

        // 커뮤니티 유저 가져오기 (커뮤니티 아이디와 유저 둘 다 일치하는 값 가져오기)

        // 해당 레코드의 isWithdraw를 true로 변환

        // 참가 인원 감소

        // 참가 인원 감소 후 모집 인원보다 작으면 모집 중으로 변경

        return null;
    }



    // 이미지 저장
    private void createCommunityImage(List<MultipartFile> multipartFileList, Community community) {

        // S3에 이미지 업로드
        List<String> communityImageUrlList = s3UploadService.communityUpload(multipartFileList);

        // 이미지 번호 1부터 시작
        Integer imgNum = 1;

        // 이미지 번호 순차적으로 증가 시키며 db에 url과 함께 저장
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
