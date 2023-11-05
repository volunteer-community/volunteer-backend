package com.maple.volunteer.service.heart;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.heart.HeartResponseDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.communityuser.CommunityUserRepository;
import com.maple.volunteer.repository.heart.HeartRepository;
import com.maple.volunteer.repository.poster.PosterRepository;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@RequiredArgsConstructor
@Service
public class HeartService {
    private final HeartRepository heartRepository;

    private final CommunityUserRepository communityUserRepository;

    private final PosterRepository posterRepository;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Transactional
    public CommonResponseDto<Object> toggleHeart(String accessToken, Long posterId, Long communityId) {

        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        //유저 존재 여부 확인
        User user = userRepository.findById(userId)
                                  // 유저가 없다면 오류 반환
                                  .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        CommunityUser communityUser = communityUserRepository.findByUserIdAndCommunityIdAndIsWithdraw(communityId, userId)
                                                             .orElseThrow(() -> new NotFoundException(ErrorCode.COMMUNITY_USER_NOT_FOUND));


        Long communityUserId = communityUser.getId();
        //게시글 존재 여부 확인
       /* Poster poster = posterRepository.findByIdAndIsDelete(posterId)
                                        .orElseThrow(() -> new NotFoundException(ErrorCode.POSTER_NOT_FOUND));*/

        Poster poster = posterRepository.findByIdAndIsDelete(posterId)
                                        .orElseThrow(() -> new NotFoundException(ErrorCode.POSTER_NOT_FOUND));

        // 기존 좋아요 존재
        Heart existingHeart = heartRepository.findUserAndPoster(communityUserId, posterId);


        if (existingHeart == null) {
            //기존 데이터가 없다면 1. 좋아요 데이터 생성

            Heart newHeart = new Heart().builder()
                                        .status(true)
                                        .communityUser(communityUser)
                                        .poster(poster)
                                        .build();

            //poster.heartIncrease();
            posterRepository.updateHeartCountIncrease(posterId);
            heartRepository.save(newHeart);

            HeartResponseDto heartResponseDto = HeartResponseDto.builder()
                                                                .heartStatus(true)
                                                                .build();
            return commonService.successResponse(SuccessCode.HEART_TOGGLE_SUCCESS.getDescription(), HttpStatus.CREATED,heartResponseDto);
        } else {
            //기존에 데이터가 있다면 2. 좋아요 상태를 변경
            Long heartId = existingHeart.getId();

            if (existingHeart.getStatus()) {
                //status:true -> false ( 좋아요 취소 )

                //poster.heartDecrease();
                posterRepository.updateHeartCountDecrease(posterId);
                heartRepository.updateStatus(heartId, false);
                HeartResponseDto heartResponseDto = HeartResponseDto.builder()
                                                                    .heartStatus(false)
                                                                    .build();
                return commonService.successResponse(SuccessCode.HEART_TOGGLE_CANCEL_SUCCESS.getDescription(), HttpStatus.CREATED,heartResponseDto);

            } else {
                //status:false -> true ( 좋아요 다시 생성 )

                //poster.heartIncrease();
                posterRepository.updateHeartCountIncrease(posterId);
                heartRepository.updateStatus(heartId, true);
                HeartResponseDto heartResponseDto = HeartResponseDto.builder()
                                                                    .heartStatus(true)
                                                                    .build();
                return commonService.successResponse(SuccessCode.HEART_TOGGLE_SUCCESS.getDescription(), HttpStatus.CREATED,heartResponseDto);
            }
        }

    }


}
