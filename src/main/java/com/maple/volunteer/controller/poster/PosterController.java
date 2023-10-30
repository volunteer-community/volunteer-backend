package com.maple.volunteer.controller.poster;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.poster.PosterRequestDto;
import com.maple.volunteer.dto.poster.PosterUpdateDto;
import com.maple.volunteer.service.poster.PosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/maple")
public class PosterController {

    private final PosterService posterService;


    // 게시글 생성
    // 작성자 토큰에서 받아오는거로 변경 해야함
    @PostMapping("/poster/community/{communityId}")
    public ResponseEntity<ResultDto<Void>> posterCreate(@PathVariable Long communityId,
                                                        @RequestPart(value = "file") MultipartFile multipartFile,
                                                        @RequestPart(value = "data") PosterRequestDto posterRequestDto) {

        CommonResponseDto<Object> posterCreate = posterService.posterCreate(communityId, multipartFile, posterRequestDto);
        ResultDto<Void> result = ResultDto.in(posterCreate.getStatus(), posterCreate.getMessage());

        return ResponseEntity.status(posterCreate.getHttpStatus()).body(result);
    }

    @PutMapping("/poster/{posterId}")
    public ResponseEntity<ResultDto<Void>> posterUpdate(@PathVariable Long posterId,
                                                        @RequestPart(value = "file") MultipartFile multipartFile,
                                                        @RequestPart(value = "data") PosterUpdateDto posterUpdateDto) {
        CommonResponseDto<Object> posterUpdate = posterService.posterUpdate(posterId,multipartFile,posterUpdateDto);
        ResultDto<Void> result = ResultDto.in(posterUpdate.getStatus(), posterUpdate.getMessage());

        return ResponseEntity.status(posterUpdate.getHttpStatus()).body(result);
    }


}
