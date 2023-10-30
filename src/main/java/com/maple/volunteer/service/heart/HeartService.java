package com.maple.volunteer.service.heart;

import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.heart.HeartRequestDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.heart.HeartRepository;
import com.maple.volunteer.repository.poster.PosterRepository;
import com.maple.volunteer.repository.user.UserRepository;
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

    private final UserRepository userRepository;

    private final PosterRepository posterRepository;

    private final CommonService commonService;


    @Transactional
    public CommonResponseDto<Object> toggleHeart(HeartRequestDto heartRequestDto) {
        Long userId = heartRequestDto.getUserId();
        Long posterId = heartRequestDto.getPosterId();

        //유저 존재 여부 확인
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        //게시글 존재 여부 확인
        Poster poster = posterRepository.findById(posterId)
                                        .orElseThrow(() -> new NotFoundException(ErrorCode.POSTER_NOT_FOUND));

        // 기존 좋아요 존재
        Heart existingHeart = heartRepository.findUserAndPoster(userId, posterId);


        if (existingHeart == null) {
            //기존 데이터가 없다면 1. 좋아요 데이터 생성

            Heart newHeart = new Heart().builder()
                                        .status(true)
                                        .user(user)
                                        .poster(poster)
                                        .build();

            poster.heartIncrease();
            heartRepository.save(newHeart);

        } else {
            //기존에 데이터가 있다면 2. 좋아요 상태를 변경
            Long heartId = existingHeart.getId();

            if (existingHeart.getStatus()) {
                //status:true -> false ( 좋아요 취소 )
                poster.heartDecrease();
                heartRepository.updateStatus(heartId,false);


            } else {
                //status:false -> true ( 좋아요 다시 생성 )
                poster.heartIncrease();
                heartRepository.updateStatus(heartId,true);
            }
        }


        return commonService.successResponse(SuccessCode.HEART_TOGGLE_SUCCESS.getDescription(), HttpStatus.CREATED, null);
    }



}