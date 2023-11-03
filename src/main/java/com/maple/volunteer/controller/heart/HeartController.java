package com.maple.volunteer.controller.heart;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.example.ExampleDto;
import com.maple.volunteer.dto.heart.HeartRequestDto;
import com.maple.volunteer.dto.heart.HeartResponseDto;
import com.maple.volunteer.service.heart.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maple")
public class HeartController {
    //TODO: 좋아요 기능구현 1. userId 로그인 구현하고 @RequestParam 변경해야함

    private final HeartService heartService;


    //좋아요 토글 방식
    @PostMapping("/like/poster/{posterId}/community")
    public ResponseEntity<ResultDto<HeartResponseDto>> toggleHeart(@RequestHeader("Authorization") String accessToken,
                                                                   @PathVariable Long posterId,
                                                                   @RequestParam("communityId") Long communityId) {
        CommonResponseDto<Object> addHeart = heartService.toggleHeart(accessToken,posterId,communityId);
        ResultDto<HeartResponseDto> result = ResultDto.in(addHeart.getStatus(), addHeart.getMessage());
        result.setData((HeartResponseDto)addHeart.getData());
        return ResponseEntity.status(addHeart.getHttpStatus()).body(result);
    }

}