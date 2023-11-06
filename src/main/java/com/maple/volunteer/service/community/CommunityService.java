package com.maple.volunteer.service.community;

import com.maple.volunteer.domain.category.Category;
import com.maple.volunteer.domain.comment.Comment;
import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.domain.communityimg.CommunityImg;
import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.dto.community.*;
import com.maple.volunteer.exception.BadRequestException;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.category.CategoryRepository;
import com.maple.volunteer.repository.comment.CommentRepository;
import com.maple.volunteer.repository.community.CommunityRepository;
import com.maple.volunteer.repository.communityimg.CommunityImgRepository;
import com.maple.volunteer.repository.communityuser.CommunityUserRepository;
import com.maple.volunteer.repository.heart.HeartRepository;
import com.maple.volunteer.repository.poster.PosterRepository;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final HeartRepository heartRepository;
    private final CategoryRepository categoryRepository;
    private final CommunityRepository communityRepository;
    private final CommunityUserRepository communityUserRepository;
    private final CommunityImgRepository communityImgRepository;
    private final PosterRepository posterRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;

    // 커뮤니티 생성
    @Transactional
    public CommonResponseDto<Object> communityCreate(String accessToken, String categoryType, List<MultipartFile> multipartFileList, CommunityRequestDto communityRequestDto) {


        // UserId 가져오기
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));

        // 유저 가져오기
        User user = userRepository.findById(userId)
                // 유저가 없다면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // 유저 닉네임 가져오기
        String nickName = user.getNickname();

        // 카테고리 가져오기
        Category category = categoryRepository.findByCategoryType(categoryType)
                                              // 카테고리 값 없으면 오류 반환
                                              .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_TYPE_NOT_FOUND));


        // 토큰 값으로 Author 추가
        // Author 값을 따로 넣어주기 위해 따로
        Community community = Community.builder()
                                       .title(communityRequestDto.getCommunityTitle())
                                       .participant(0)
                                       .maxParticipant(communityRequestDto.getCommunityMaxParticipant())
                                       .author(nickName)
                                       .content(communityRequestDto.getCommunityContent())
                                       .status(CommunityStatus.COMMUNITY_RECRUITMENT_ING.getDescription())
                                       .location(communityRequestDto.getCommunityLocation())
                                       .isDelete(false)
                                       .category(category)
                                       .build();

        // 커뮤니티 저장
        Long communityId = communityRepository.save(community).getId();

        // S3에 이미지 저장
        createCommunityImage(multipartFileList, community);


        // 생성한 인원은 바로 가입 처리
        // 커뮤니티 가져오기
        Community communityGet = communityRepository.findById(communityId)
                // 커뮤니티가 없다면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        // 커뮤니티 유저 생성
        CommunityUser communityUser = CommunityUser.builder()
                .user(user)
                .community(communityGet)
                .isWithdraw(false)
                .build();

        // 커뮤니티 유저 저장
        communityUserRepository.save(communityUser);

        // 참가 인원 증가
        communityRepository.participantIncrease(communityId);


        return commonService.successResponse(SuccessCode.COMMUNITY_CREATE_SUCCESS.getDescription(), HttpStatus.CREATED, null);
    }

    // 모든 커뮤니티 조회 (페이지 네이션)
    public CommonResponseDto<Object> allCommunityInquiry(int page, int size, String sortBy) {

        // 페이지, 요소 개수, 정렬 설정
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(sortBy)
                                                                  .descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityRepository.findAllCommunityList(pageable);

        if (data.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMUNITY_NOT_FOUND);
        }

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
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(sortBy)
                                                                  .descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityRepository.findCommunityListByCategoryType(categoryId, pageable);

        if (data.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMUNITY_NOT_FOUND);
        }

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
    public CommonResponseDto<Object> searchTitleCommunityInquiry(String keyword, int page, int size, String sortBy) {

        // 페이지, 요소 개수, 정렬 설정
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(sortBy)
                                                                  .descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityRepository.findCommunityListBySearchTitle(keyword, pageable);

        if (data.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMUNITY_NOT_FOUND);
        }
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
    public CommonResponseDto<Object> searchAuthorCommunityInquiry(String keyword, int page, int size, String sortBy) {

        // 페이지, 요소 개수, 정렬 설정
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(sortBy)
                                                                  .descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityRepository.findCommunityListBySearchAuthor(keyword, pageable);

        if (data.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMUNITY_NOT_FOUND);
        }

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

    // 커뮤니티 수정
    @Transactional
    public CommonResponseDto<Object> communityUpdate(String accessToken, Long communityId, List<MultipartFile> multipartFileList, CommunityRequestDto communityRequestDto) {

        // 카테고리 가져오기
        Category category = categoryRepository.findByCategoryId(communityRequestDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_ID_NOT_FOUND));

        // UserId 가져오기
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));

        // 유저 가져오기
        User user = userRepository.findById(userId)
                // 유저가 없다면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // 유저 닉네임 가져오기
        String nickName = user.getNickname();

        // 커뮤니티 가져오기
        Community community = communityRepository.findCommunityByFalse(communityId)
                // 커뮤니티가 없으면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        // 작성자와 현재 로그인한 유저의 닉네임이 일치한지
        if (!community.getAuthor()
                      .equals(nickName)) {
            throw new BadRequestException(ErrorCode.AUTHOR_NOT_EQUAL);
        }

        if (community.getParticipant() > communityRequestDto.getCommunityMaxParticipant()) {  // 참여 인원보다 작을 때
            throw new BadRequestException(ErrorCode.MAX_PARTICIPANT_LOW_ERROR);
        }

        // 이미지 url 값만 가져오기
        List<CommunityImgPathDto> communityImgPathList = communityImgRepository.findCommunityImgPathList(communityId);

        // url 값 삭제
        for (CommunityImgPathDto communityImgPathDto : communityImgPathList) {
            String imgPath = communityImgPathDto.getCommunityImgPath();

            // s3 이미지 삭제
            s3UploadService.deleteCommunityImg(imgPath);
        }

        // db에 url 삭제
        communityImgRepository.deleteByCommunityId(communityId);

        // 받아온 데이터 업데이트
        community.communityUpdate(category, communityRequestDto.getCommunityTitle(), community.getParticipant(),
                communityRequestDto.getCommunityMaxParticipant(), community.getAuthor(),
                community.getStatus(), communityRequestDto.getCommunityContent(), communityRequestDto.getCommunityLocation());

        if (community.getParticipant().equals(communityRequestDto.getCommunityMaxParticipant())) {    // 참여 인원과 같을 때
            community.communityRecruitmentEnd();
        }

        if (community.getParticipant() < community.getMaxParticipant() && community.getStatus()
                                                                                   .equals(CommunityStatus.COMMUNITY_RECRUITMENT_END.getDescription())) { // 상태가 모집 마감일 때
            community.communityRecruitmentIng();
        }

        // 이미지 새로 업로드
        createCommunityImage(multipartFileList, community);

        return commonService.successResponse(SuccessCode.COMMUNITY_UPDATE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 커뮤니티 삭제
    @Transactional
    public CommonResponseDto<Object> communityDelete(String accessToken, Long communityId) {

        // UserId 가져오기
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));

        // 유저 가져오기
        User user = userRepository.findById(userId)
                // 유저가 없다면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // 유저 닉네임 가져오기
        String nickName = user.getNickname();

        // 커뮤니티 가져오기
        Community community = communityRepository.findCommunityByFalse(communityId)
                // 커뮤니티가 없다면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

        // 작성자와 현재 로그인한 유저의 닉네임이 일치한지
        if (!community.getAuthor()
                      .equals(nickName)) {
            throw new BadRequestException(ErrorCode.AUTHOR_NOT_EQUAL);
        }


        // 해당 커뮤니티에 속하는 게시글, 댓글, 커뮤니티 유저, 좋아요 모두 삭제
        commentRepository.CommentDeleteByCommunityId(communityId, true);
        heartRepository.updateStatusByCommunityId(communityId, false);
        posterRepository.PosterDeleteByCommunityId(communityId, true);
        communityUserRepository.CommunityUserDelete(communityId, true);


        // 이미지 url 값만 가져오기
        List<CommunityImg> communityImgPathList = communityImgRepository.findDeletedCommunityImgPathList(communityId);

        // url 값 삭제
        for (CommunityImg communityImgPath : communityImgPathList) {
            String imgPath = communityImgPath.getImagePath();

            // s3 이미지 삭제
            s3UploadService.deleteCommunityImg(imgPath);

            // DB isDelete = true 로 변경
            communityImgRepository.deleteByCommunityImgId(communityImgPath.getId(),true);
        }

        // isDelete 값을 true로 변경
        communityRepository.deleteCommunityId(communityId, true);

        return commonService.successResponse(SuccessCode.COMMUNITY_DELETE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 커뮤니티 참가
    @Transactional
    public CommonResponseDto<Object> communitySignup(String accessToken, Long communityId) {

        // UserId 가져오기
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));

        // 유저 가져오기
        User user = userRepository.findById(userId)
                                  // 유저가 없다면 오류 반환
                                  .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // 커뮤니티 재가입 회원인지
        Optional<CommunityUser> communityUserOptional = communityUserRepository.findByUserIdAndCommunityId(communityId, userId);

        if (communityUserOptional.isPresent()) {
            CommunityUser communityUser = communityUserOptional.get();

            // 이미 가입한 회원인지
            if (!communityUser.getIsWithdraw()) {
                throw new BadRequestException(ErrorCode.COMMUNITY_USER_DUPLICATE);
            }

            // 커뮤니티 가져오기
            Community community = communityUser.getCommunity();

            // 현재 상태가 모집 마감이면 오류 반환
            if (community.getStatus()
                         .equals(CommunityStatus.COMMUNITY_RECRUITMENT_END.getDescription())) {
                throw new BadRequestException(ErrorCode.COMMUNITY_RECRUITMENT_END_ERROR);
            }

            // 커뮤니티 참가 인원 증가
            community.communityParticipantIncrease();

            // 참가 인원 증가 후 모집 인원과 동일해지면 모집 마감으로 변경
            if (community.getParticipant()
                         .equals(community.getMaxParticipant())) {
                community.communityRecruitmentEnd();
            }

            // 커뮤니티 재가입
            communityUser.communityReSign();

            return commonService.successResponse(SuccessCode.COMMUNITY_RE_SIGNUP_SUCCESS.getDescription(), HttpStatus.OK, null);

            // 재가입 회원이 아닐 경우
        } else {

            // 커뮤니티 가져오기
            Community community = communityRepository.findById(communityId)
                                                     // 커뮤니티가 없다면 오류 반환
                                                     .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));

            // 현재 상태가 모집 마감이면 오류 반환
            if (community.getStatus()
                         .equals(CommunityStatus.COMMUNITY_RECRUITMENT_END.getDescription())) {
                throw new BadRequestException(ErrorCode.COMMUNITY_RECRUITMENT_END_ERROR);
            }

            // 커뮤니티 유저 생성
            CommunityUser communityUser = CommunityUser.builder()
                                                       .user(user)
                                                       .community(community)
                                                       .isWithdraw(false)
                                                       .build();

            // 커뮤니티 유저 저장
            communityUserRepository.save(communityUser);

            // 참가 인원 증가
            community.communityParticipantIncrease();

            // 참가 인원 증가 후 모집 인원과 동일해지면 모집 마감으로 변경
            if (community.getParticipant()
                         .equals(community.getMaxParticipant())) {
                community.communityRecruitmentEnd();
            }

            return commonService.successResponse(SuccessCode.COMMUNITY_SIGNUP_SUCCESS.getDescription(), HttpStatus.CREATED, null);
        }
    }

    // 커뮤니티 나가기
    @Transactional
    public CommonResponseDto<Object> communityWithdraw(String accessToken, Long communityId) {

        // UserId 가져오기
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));

        // 커뮤니티 유저 가져오기 (커뮤니티 아이디와 유저 둘 다 일치하는 값 가져오기)
        CommunityUser communityUser = communityUserRepository.findByUserIdAndCommunityId(communityId, userId)
                                                             .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_USER_NOT_FOUND));

        // 커뮤니티 가져오기
        Community community = communityUser.getCommunity();

        // 해당 레코드의 isWithdraw를 true로 변환
        communityUser.communityWithdraw();

        // 참가 인원 감소
        community.communityParticipantDecrease();

        // 참가 인원 감소 후 모집 인원보다 작으면 모집 중으로 변경
        if (community.getParticipant() < community.getMaxParticipant()) {
            community.communityRecruitmentIng();
        }


        // 탈퇴하는 유저ID가 작성한 게시글 (게시글 좋아요, 좋아요 개수 0, 댓글 삭제) 삭제

        //1. 유저ID에 해당되는 게시글 리스트 가져오기
        List<Poster> posterList = posterRepository.findByPosterListUserId(userId);

        for (Poster eachPoster : posterList) {
            Long posterId = eachPoster.getId();

            //게시글 Id에 해당되는 heartCount = 0 으로 변경
            posterRepository.updateHeartCountZero(posterId);

            //게시글 ID에 해당되는 댓글 삭제(isDeleted = true)
            commentRepository.commentDeleteByPosterId(posterId);

            //유저 ID에 해당하는 좋아요 리스트 가져오기
            List<Heart> heartList = heartRepository.findHeartListPosterId(posterId);

            for (Heart eachHeart : heartList) {
                Long heartId = eachHeart.getId();

                //가져온 좋아요를 false로 바꿔주기(좋아요 삭제)
                heartRepository.updateStatus(heartId, false);

            }

        }

        // 탈퇴하는 유저ID가 누른 타인의 게시글에서 좋아요 개수 -1 시키기 , 좋아요 false 시키기

        List<Heart> heartListByPosterId = heartRepository.findHeartListUserId(userId);

        for(Heart eachHeartPosterId : heartListByPosterId){

            Long heartId = eachHeartPosterId.getId();
            Long posterId = eachHeartPosterId.getPoster().getId();

            heartRepository.updateStatus(heartId,false);
            posterRepository.updateHeartCountDecrease(posterId);
        }

        //유저Id에 해당되는 게시글 삭제/댓글 삭제

        posterRepository.PosterDeleteByUserId(userId, true);
        commentRepository.CommentDeleteByUserId(userId, true);


        return commonService.successResponse(SuccessCode.COMMUNITY_WITHDRAW_SUCCESS.getDescription(), HttpStatus.OK, null);
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