package com.maple.volunteer.controller.community;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.community.CommunityDetailAndImgResponseDto;
import com.maple.volunteer.dto.community.CommunityListResponseDto;
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
    @PostMapping("/community")
    public ResponseEntity<ResultDto<Void>> communityCreate(@RequestHeader("Authorization") String accessToken,
                                                           @RequestParam String categoryType,
                                                           @RequestPart(value = "imageList") List<MultipartFile> multipartFileList,
                                                           @RequestPart(value = "communityRequestDto") CommunityRequestDto communityRequestDto) {

        CommonResponseDto<Object> communityCreate = communityService.communityCreate(accessToken, categoryType, multipartFileList, communityRequestDto);
        ResultDto<Void> result = ResultDto.in(communityCreate.getStatus(), communityCreate.getMessage());

        return ResponseEntity.status(communityCreate.getHttpStatus()).body(result);
    }

    // 모든 커뮤니티 리스트 API (페이지 네이션)
    @GetMapping("/community")
    public ResponseEntity<ResultDto<CommunityListResponseDto>> allCommunityInquiry(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                                                   @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                                   @RequestParam(value = "sortBy", defaultValue = "modifiedAt", required = false) String sortBy) {

        CommonResponseDto<Object> allCommunityInquiry = communityService.allCommunityInquiry(page, size, sortBy);
        ResultDto<CommunityListResponseDto> result = ResultDto.in(allCommunityInquiry.getStatus(), allCommunityInquiry.getMessage());
        result.setData((CommunityListResponseDto) allCommunityInquiry.getData());

        return ResponseEntity.status(allCommunityInquiry.getHttpStatus()).body(result);
    }

    // 커뮤니티 상세 보기 API
    @GetMapping("/community/{communityId}")
    public ResponseEntity<ResultDto<CommunityDetailAndImgResponseDto>> communityDetailInquiry(@PathVariable Long communityId) {

        CommonResponseDto<Object> communityDetailInquiry = communityService.communityDetailInquiry(communityId);
        ResultDto<CommunityDetailAndImgResponseDto> result = ResultDto.in(communityDetailInquiry.getStatus(), communityDetailInquiry.getMessage());
        result.setData((CommunityDetailAndImgResponseDto) communityDetailInquiry.getData());

        return ResponseEntity.status(communityDetailInquiry.getHttpStatus()).body(result);
    }
}
