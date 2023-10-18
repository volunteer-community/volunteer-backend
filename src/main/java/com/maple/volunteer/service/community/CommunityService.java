package com.maple.volunteer.service.community;

import com.maple.volunteer.domain.category.Category;
import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.domain.communityimg.CommunityImg;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.community.CommunityDetailAndImgResponseDto;
import com.maple.volunteer.dto.community.CommunityDetailResponseDto;
import com.maple.volunteer.dto.community.CommunityImgResponseDto;
import com.maple.volunteer.dto.community.CommunityRequestDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.category.CategoryRepository;
import com.maple.volunteer.repository.community.CommunityRepository;
import com.maple.volunteer.repository.communityimg.CommunityImgRepository;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.service.s3upload.S3UploadService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
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
    private final CommunityImgRepository communityImgRepository;
    private final S3UploadService s3UploadService;
    private final CommonService commonService;

    // 커뮤니티 생성
    @Transactional
    public CommonResponseDto<Object> communityCreate(String accessToken, Long categoryId, List<MultipartFile> multipartFileList, CommunityRequestDto communityRequestDto) {


        Category category = categoryRepository.findCategoryId(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        Community community = communityRequestDto.toEntity(category);
        communityRepository.save(community);

        createCommunityImage(multipartFileList, community);


        return commonService.successResponse(SuccessCode.COMMUNITY_CREATE_SUCCESS.getDescription(), HttpStatus.CREATED, null);

    }

    // 커뮤니티 상세 조회
    public CommonResponseDto<Object> communityDetailInquiry(String accessToken, Long communityId) {

        CommunityDetailResponseDto communityDetailResponseDto = communityRepository.findCommunityDetailByCommunityId(communityId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        List<CommunityImgResponseDto> communityImgResponseDtoList = communityImgRepository.findCommunityImgListByCommunityId(communityId);

        CommunityDetailAndImgResponseDto communityDetailAndImgResponseDto = CommunityDetailAndImgResponseDto.builder()
                .communityDetail(communityDetailResponseDto)
                .communityImgPathList(communityImgResponseDtoList)
                .build();

        return commonService.successResponse(SuccessCode.COMMUNITY_DETAIL_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, communityDetailAndImgResponseDto);
    }

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
