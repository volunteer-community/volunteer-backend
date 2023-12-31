package com.maple.volunteer.controller.poster;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.poster.PosterDetailListResponseDto;
import com.maple.volunteer.dto.poster.PosterListResponseDto;
import com.maple.volunteer.dto.poster.PosterRequestDto;
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


    //cummunityId에 해당하는 게시글 리스트
    //페이징 추가해야함
    @GetMapping("/poster/community/{communityId}")
    public ResponseEntity<ResultDto<PosterListResponseDto>> allPosterInquiry(@RequestHeader("Authorization") String accessToken,
                                                                             @PathVariable Long communityId,
                                                                             @RequestParam(value = "page", defaultValue = "1",required = false) int page,
                                                                             @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                             @RequestParam(value = "sortBy", defaultValue = "updatedAt", required = false) String sortBy) {
        CommonResponseDto<Object> allPosterInquiry = posterService.allPosterInquiry(accessToken, communityId, page, size, sortBy);
        ResultDto<PosterListResponseDto> result = ResultDto.in(allPosterInquiry.getStatus(), allPosterInquiry.getMessage());
        result.setData((PosterListResponseDto) allPosterInquiry.getData());

        return ResponseEntity.status(allPosterInquiry.getHttpStatus()).body(result);
    }

    //posterId 상세보기
    @GetMapping("/poster/{posterId}/community")
    public ResponseEntity<ResultDto<PosterDetailListResponseDto>> posterDetailInquiry(@RequestHeader("Authorization") String accessToken,
                                                                                      @PathVariable Long posterId,
                                                                                      @RequestParam(value = "communityId") Long communityId){
        CommonResponseDto<Object> posterDetailInquiry = posterService.posterDetailInquiry(accessToken, posterId, communityId);
        ResultDto<PosterDetailListResponseDto> result = ResultDto.in(posterDetailInquiry.getStatus(), posterDetailInquiry.getMessage());
        result.setData((PosterDetailListResponseDto) posterDetailInquiry.getData());

        return ResponseEntity.status(posterDetailInquiry.getHttpStatus()).body(result);
    }


    // 게시글 생성
    // 작성자 토큰에서 받아오는거로 변경 해야함
    @PostMapping("/poster/community/{communityId}")
    public ResponseEntity<ResultDto<Void>> posterCreate(@RequestHeader("Authorization") String accessToken,
                                                        @PathVariable Long communityId,
                                                        @RequestPart(value = "file") MultipartFile multipartFile,
                                                        @RequestPart(value = "data") PosterRequestDto posterRequestDto) {

        CommonResponseDto<Object> posterCreate = posterService.posterCreate(accessToken, communityId, multipartFile, posterRequestDto);
        ResultDto<Void> result = ResultDto.in(posterCreate.getStatus(), posterCreate.getMessage());

        return ResponseEntity.status(posterCreate.getHttpStatus()).body(result);
    }

    // 게시글 수정
    @PutMapping("/poster/{posterId}/community")
    public ResponseEntity<ResultDto<Void>> posterUpdate(@RequestHeader("Authorization") String accessToken,
                                                        @PathVariable Long posterId,
                                                        @RequestParam(value = "communityId") Long communityId,
                                                        @RequestPart(value = "file") MultipartFile multipartFile,
                                                        @RequestPart(value = "data") PosterRequestDto posterRequestDto) {
        CommonResponseDto<Object> posterUpdate = posterService.posterUpdate(accessToken, posterId, communityId, multipartFile, posterRequestDto);
        ResultDto<Void> result = ResultDto.in(posterUpdate.getStatus(), posterUpdate.getMessage());

        return ResponseEntity.status(posterUpdate.getHttpStatus()).body(result);
    }

    // 게시글 posterId에 해당 되는 글만 삭제
    @DeleteMapping("/poster/{posterId}/community")
    public ResponseEntity<ResultDto<Void>> posterDeleteByCommentId(@RequestHeader("Authorization") String accessToken,
                                                                                @PathVariable Long posterId,
                                                                                @RequestParam(value = "communityId") Long communityId) {

        CommonResponseDto<Object> posterDelete = posterService.posterDeleteByPosterId(accessToken, posterId, communityId);
        ResultDto<Void> result = ResultDto.in(posterDelete.getStatus(), posterDelete.getMessage());

        return ResponseEntity.status(posterDelete.getHttpStatus())
                .body(result);
    }

}