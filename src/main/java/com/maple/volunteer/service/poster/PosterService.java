package com.maple.volunteer.service.poster;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.poster.Poster;

import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.dto.poster.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import com.maple.volunteer.domain.posterimg.PosterImg;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.poster.PosterRequestDto;
import com.maple.volunteer.dto.poster.PosterUpdateDto;
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

    private final CommonService commonService;

    private final PosterRepository posterRepository;
    private final CommunityUserRepository communityUserRepository;
    private final S3UploadService s3UploadService;
    private final PosterImgRepository posterImgRepository;


    //TODO 데이터가 비어있는지 확인하는 예외처리 필요함
    //TODO: userID & communityID & iswithDraw(false)
    //전체조회
    public CommonResponseDto<Object> allPosterInquiry(Long communityId, int page, int size, String sortBy) {

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
    public CommonResponseDto<Object> posterDetailInquiry(Long posterId, Long communityId) {

        PosterDetailResponseDto posterDetailResponseDto = posterRepository.findPosterDetailByCommunityIdAndPosterId(communityId, posterId)
                                                                          .orElseThrow(() -> new NotFoundException(ErrorCode.POSTER_NOT_FOUND));

        PosterDetailListResponseDto posterDetailListResponseDto = PosterDetailListResponseDto.builder()
                                                                                             .posterDetail(posterDetailResponseDto)
                                                                                             .build();

        return commonService.successResponse(SuccessCode.POSTER_DETAIL_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, posterDetailListResponseDto);
    }





    // 게시글 생성
    @Transactional
    public CommonResponseDto<Object> posterCreate(Long communityId,
                                                  MultipartFile multipartFile, PosterRequestDto posterRequestDto) {

        CommunityUser communityUser = communityUserRepository.findByCommunityId(communityId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.COMMUNITY_NOT_FOUND));
        // communityUser레포에서 커뮤니티유저 아이디 받아옴
        Poster poster = posterRequestDto.toEntity(communityUser);
        posterRepository.save(poster);

        addPosterImg(multipartFile, poster);

        return commonService.successResponse(SuccessCode.POSTER_CREATE_SUCCESS.getDescription(), HttpStatus.CREATED, null);
    }

    // 게시글 수정
    @Transactional
    public CommonResponseDto<Object> posterUpdate(Long posterId, MultipartFile multipartFile,
                                                  PosterUpdateDto posterUpdateDto) {

        Optional<Poster> findPoster = posterRepository.findById(posterId);

        if(findPoster.isPresent()){
            Poster poster = findPoster.get();

            poster.setTitle(posterUpdateDto.getPosterTitle());
            poster.setContent(posterUpdateDto.getPosterContent());

            updatePosterImg(multipartFile, poster);

            // db삭제
            posterImgRepository.deleteAllByPoster(poster);

            // s3와 db에 새로운 이미지 업로드
            addPosterImg(multipartFile, poster);

            // 정보 수정
            posterRepository.save(poster);

            return commonService.successResponse(SuccessCode.POSTER_UPDATE_SUCCESS.getDescription(), HttpStatus.OK, null);
        }
        else {
            return commonService.errorResponse(ErrorCode.POSTER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND, null);
        }

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

            int startIndex = posterImgUrl.indexOf("image/poster/");
            String filename = posterImgUrl.substring(startIndex);

            // s3 기존 이미지 삭재
            s3UploadService.deleteImg(filename);
        }

    }



}
