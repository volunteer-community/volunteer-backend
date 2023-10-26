package com.maple.volunteer.controller.poster;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;

import com.maple.volunteer.dto.poster.PosterDetailListResponseDto;
import com.maple.volunteer.dto.poster.PosterListResponseDto;
import com.maple.volunteer.service.poster.PosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maple")
public class PosterController {

    private final PosterService posterService;

    //cummunityId에 해당하는 게시글 리스트
    //페이징 추가해야함
    @GetMapping("/poster/community/{communityId}")
    public ResponseEntity<ResultDto<PosterListResponseDto>> allPosterInquiry(@PathVariable Long communityId,
                                                                             @RequestParam(value = "page", defaultValue = "1",required = false) int page,
                                                                             @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                             @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy) {
        CommonResponseDto<Object>  allPosterInquiry = posterService.allPosterInquiry(communityId,page,size,sortBy);
        ResultDto<PosterListResponseDto> result = ResultDto.in(allPosterInquiry.getStatus(), allPosterInquiry.getMessage());
        result.setData((PosterListResponseDto) allPosterInquiry.getData());

        return ResponseEntity.status(allPosterInquiry.getHttpStatus()).body(result);
    }

    //posterId 상세보기
    @GetMapping("/poster/{posterId}/community/{communityId}")
    public ResponseEntity<ResultDto<PosterDetailListResponseDto>> posterDetailInquiry(@PathVariable Long posterId,
                                                                                                  @PathVariable Long communityId){
        CommonResponseDto<Object> posterDetailInquiry = posterService.posterDetailInquiry(posterId,communityId);
        ResultDto<PosterDetailListResponseDto> result = ResultDto.in(posterDetailInquiry.getStatus(), posterDetailInquiry.getMessage());
        result.setData((PosterDetailListResponseDto) posterDetailInquiry.getData());

        return ResponseEntity.status(posterDetailInquiry.getHttpStatus()).body(result);
    }




}
