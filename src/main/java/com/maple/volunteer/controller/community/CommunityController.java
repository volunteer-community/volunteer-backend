package com.maple.volunteer.controller.community;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.community.CommunityDetailAndImgResponseDto;
import com.maple.volunteer.dto.community.CommunityRequestDto;
import com.maple.volunteer.service.community.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CommunityController {

    private final CommunityService communityService;

    // 커뮤니티 생성 API
    @PostMapping("/community/{categoryId}")
    public ResponseEntity<ResultDto<Void>> communityCreate(@RequestHeader("Authorization") String accessToken,
                                                           @PathVariable Long categoryId,
                                                           @RequestPart(value = "imageList") List<MultipartFile> multipartFileList,
                                                           @RequestPart(value = "communityRequestDto") CommunityRequestDto communityRequestDto) {

        CommonResponseDto<Object> communityCreate = communityService.communityCreate(accessToken, categoryId, multipartFileList, communityRequestDto);
        ResultDto<Void> result = ResultDto.in(communityCreate.getStatus(), communityCreate.getMessage());

        return ResponseEntity.status(communityCreate.getHttpStatus()).body(result);
    }

    // 커뮤니티 상세 보기 API
    @GetMapping("/community/{communityId}")
    public ResponseEntity<ResultDto<CommunityDetailAndImgResponseDto>> communityDetailInquiry(@RequestHeader("Authorization") String accessToken,
                                                                  @PathVariable Long communityId) {

        CommonResponseDto<Object> communityDetailInquiry = communityService.communityDetailInquiry(accessToken, communityId);
        ResultDto<CommunityDetailAndImgResponseDto> result = ResultDto.in(communityDetailInquiry.getStatus(), communityDetailInquiry.getMessage());
        result.setData((CommunityDetailAndImgResponseDto) communityDetailInquiry.getData());

        return ResponseEntity.status(communityDetailInquiry.getHttpStatus()).body(result);
    }
}
