package com.maple.volunteer.service.poster;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.poster.Poster;

import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.dto.poster.*;
import com.maple.volunteer.exception.BadRequestException;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import com.maple.volunteer.domain.posterimg.PosterImg;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.poster.PosterRequestDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.communityuser.CommunityUserRepository;
import com.maple.volunteer.repository.poster.PosterRepository;
import com.maple.volunteer.repository.posterimg.PosterImgRepository;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.service.s3upload.S3UploadService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PosterService {

    private final JwtUtil jwtUtil;
    private final CommonService commonService;
    private final S3UploadService s3UploadService;
    private final UserRepository userRepository;
    private final PosterRepository posterRepository;
    private final CommunityUserRepository communityUserRepository;
    private final PosterImgRepository posterImgRepository;



    //전체조회
    public CommonResponseDto<Object> allPosterInquiry(String accessToken, Long communityId, int page, int size, String sortBy) {

        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        communityUserRepository.findByUserIdAndCommunityIdAndIsWithdraw(communityId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_USER_NOT_FOUND));

        Optional<Boolean> posterExists = posterRepository.existsByCommunityId(communityId);

        if (!posterExists.orElse(false)) {
            throw new NotFoundException(ErrorCode.POSTER_NOT_FOUND);
        }

        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(sortBy)
                                                                  .descending());


        Page<PosterResponseDto> data = posterRepository.findAllPosterList(communityId, pageable);
        List<PosterResponseDto> posterResponseList = data.getContent();

        PaginationDto paginationDto = PaginationDto.builder()
                                                   .totalPages(data.getTotalPages())
                                                   .totalElements(data.getTotalElements())
                                                   .pageNo(data.getNumber())
                                                   .isLastPage(data.isLast())
                                                   .build();

        PosterListResponseDto posterListResponseDto = PosterListResponseDto.builder()
                                                                           .posterList(posterResponseList)
                                                                           .paginationDto(paginationDto)
                                                                           .build();

        return commonService.successResponse(SuccessCode.ALL_POSTER_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, posterListResponseDto);
    }

    //상세조회
    public CommonResponseDto<Object> posterDetailInquiry(String accessToken, Long posterId, Long communityId) {
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        communityUserRepository.findByUserIdAndCommunityIdAndIsWithdraw(communityId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_USER_NOT_FOUND));

        PosterDetailResponseDto posterDetailResponseDto = posterRepository.findPosterDetailByCommunityIdAndPosterId(communityId, posterId)
                                                                          .orElseThrow(() -> new NotFoundException(ErrorCode.POSTER_NOT_FOUND));

        PosterDetailListResponseDto posterDetailListResponseDto = PosterDetailListResponseDto.builder()
                                                                                             .posterDetail(posterDetailResponseDto)
                                                                                             .build();

        return commonService.successResponse(SuccessCode.POSTER_DETAIL_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, posterDetailListResponseDto);
    }


    // 게시글 생성
    @Transactional
    public CommonResponseDto<Object> posterCreate(String accessToken, Long communityId,
                                                  MultipartFile multipartFile, PosterRequestDto posterRequestDto) {

        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        CommunityUser communityUser = communityUserRepository.findByUserIdAndCommunityIdAndIsWithdraw(communityId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_USER_NOT_FOUND));

        String nickName = user.getNickname();

        Poster poster = Poster.builder()
                .title(posterRequestDto.getPosterTitle())
                .content(posterRequestDto.getPosterContent())
                .author(nickName)
                .heartCount(0)
                .isDelete(false)
                .communityUser(communityUser)
                .build();


        posterRepository.save(poster);

        addPosterImg(multipartFile, poster);

        return commonService.successResponse(SuccessCode.POSTER_CREATE_SUCCESS.getDescription(), HttpStatus.CREATED, null);
    }

    // 게시글 수정
    @Transactional
    public CommonResponseDto<Object> posterUpdate(String accessToken, Long posterId, Long communityId,
                                                   MultipartFile multipartFile, PosterRequestDto posterRequestDto) {

        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        communityUserRepository.findByUserIdAndCommunityIdAndIsWithdraw(communityId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_USER_NOT_FOUND));
        Poster poster = posterRepository.findByIdAndIsDelete(posterId,false)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POSTER_NOT_FOUND));

        String nickName = user.getNickname();
        if (!poster.getAuthor().equals(nickName)){
            throw new BadRequestException(ErrorCode.AUTHOR_NOT_EQUAL);
        }

        poster.posterUpdate(posterRequestDto.getPosterTitle(), posterRequestDto.getPosterContent(), poster.getAuthor());

        updatePosterImg(multipartFile, poster);

        // db삭제
        posterImgRepository.deleteAllByPoster(poster);

        // s3와 db에 새로운 이미지 업로드
        addPosterImg(multipartFile, poster);

        // 정보 수정
        posterRepository.save(poster);

        return commonService.successResponse(SuccessCode.POSTER_UPDATE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 게시글 posterId에 해당 되는 글만 삭제
    @Transactional
    public CommonResponseDto<Object> posterDeleteByPosterId(String accessToken, Long posterId, Long communityId) {

        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        // 유저 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        communityUserRepository.findByUserIdAndCommunityIdAndIsWithdraw(communityId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_USER_NOT_FOUND));

        Poster poster = posterRepository.findByIdAndIsDelete(posterId,false)
                                           .orElseThrow(() -> new NotFoundException(ErrorCode.POSTER_NOT_FOUND));

        String nickName = user.getNickname();
        if(!poster.getAuthor().equals(nickName)){
            throw new BadRequestException(ErrorCode.AUTHOR_NOT_EQUAL);
        }

        // 이미지 삭제 (s3삭제)
        PosterImg posterImg = posterImgRepository.findByPoster(poster);
        String posterImgUrl = posterImg.getImagePath();
        s3UploadService.deletePosterImg(posterImgUrl);

        // DB isDelete = true 로 변경
        Long posterImgId= posterImg.getId();
        posterImgRepository.deleteByPosterImgId(posterImgId,true);

        posterRepository.posterDeleteByPosterId(posterId);
        return commonService.successResponse(SuccessCode.POSTER_DELETE_SUCCESS.getDescription(),HttpStatus.OK,null);
    }

    private void addPosterImg(MultipartFile multipartFile, Poster poster){

        String posterImgUrl = s3UploadService.posterUpload(multipartFile);
        int imgNum = 1;

        PosterImg posterImg = PosterImg.builder()
                .imagePath(posterImgUrl)
                .imageNum(imgNum)
                .poster(poster)
                .build();

        posterImgRepository.save(posterImg);
    }

    private void updatePosterImg(MultipartFile multipartFile, Poster poster){

        //기존이미지 가져오기
        if(multipartFile != null){
            PosterImg posterImg = posterImgRepository.findByPoster(poster);

            String posterImgUrl = posterImg.getImagePath();

            s3UploadService.deletePosterImg(posterImgUrl);
        }

    }

}
