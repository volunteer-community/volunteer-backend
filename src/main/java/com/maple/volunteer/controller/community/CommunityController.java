package com.maple.volunteer.controller.community;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.community.CommunityDetailAndImgResponseDto;
import com.maple.volunteer.dto.community.CommunityListResponseDto;
import com.maple.volunteer.dto.community.CommunityRequestDto;
import com.maple.volunteer.dto.community.CommunityUpdateRequestDto;
import com.maple.volunteer.service.community.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maple")
public class CommunityController {

    private final CommunityService communityService;

    // 커뮤니티 생성 API
    @PostMapping("/community")
    public ResponseEntity<ResultDto<Void>> communityCreate(@RequestHeader("Authorization") String accessToken,
                                                           @RequestParam(value = "categoryType") String categoryType,
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

    // 카테고리 별 커뮤니티 리스트 API (페이지 네이션)
    @GetMapping("/community/category")
    public ResponseEntity<ResultDto<CommunityListResponseDto>> categoryCommunityInquiry(@RequestParam(value = "categoryType") String categoryType,
                                                                                        @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                                                        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                                        @RequestParam(value = "sortBy", defaultValue = "modifiedAt", required = false) String sortBy) {

        CommonResponseDto<Object> categoryCommunityInquiry = communityService.categoryCommunityInquiry(categoryType, page, size, sortBy);
        ResultDto<CommunityListResponseDto> result = ResultDto.in(categoryCommunityInquiry.getStatus(), categoryCommunityInquiry.getMessage());
        result.setData((CommunityListResponseDto) categoryCommunityInquiry.getData());

        return ResponseEntity.status(categoryCommunityInquiry.getHttpStatus()).body(result);
    }

    // 커뮤니티 리스트 검색 API (페이지 네이션) -> 커뮤니티 제목, 작성자, 활동 장소(보류)
    @GetMapping("/community/search/{type}")
    public ResponseEntity<ResultDto<CommunityListResponseDto>> searchCommunityInquiry(@PathVariable(value = "type") String type,
                                                                                      @RequestParam(value = "keyword") String keyword,
                                                                                      @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                                                      @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                                      @RequestParam(value = "sortBy", defaultValue = "modifiedAt", required = false) String sortBy) {
        CommonResponseDto<Object> searchCommunityInquiry;

        if (type.equals("title")) {
            searchCommunityInquiry = communityService.searchTitleCommunityInquiry(keyword, page, size, sortBy);
        } else {
            searchCommunityInquiry = communityService.searchAuthorCommunityInquiry(keyword, page, size, sortBy);
        }

        ResultDto<CommunityListResponseDto> result = ResultDto.in(searchCommunityInquiry.getStatus(), searchCommunityInquiry.getMessage());
        result.setData((CommunityListResponseDto) searchCommunityInquiry.getData());

        return ResponseEntity.status(searchCommunityInquiry.getHttpStatus()).body(result);
    }

    // 커뮤니티 상세 보기 API
    @GetMapping("/community/{communityId}")
    public ResponseEntity<ResultDto<CommunityDetailAndImgResponseDto>> communityDetailInquiry(@PathVariable(value = "communityId") Long communityId) {

        CommonResponseDto<Object> communityDetailInquiry = communityService.communityDetailInquiry(communityId);
        ResultDto<CommunityDetailAndImgResponseDto> result = ResultDto.in(communityDetailInquiry.getStatus(), communityDetailInquiry.getMessage());
        result.setData((CommunityDetailAndImgResponseDto) communityDetailInquiry.getData());

        return ResponseEntity.status(communityDetailInquiry.getHttpStatus()).body(result);
    }

    // 커뮤니티 수정 API
    @PutMapping("/community/{communityId}")
    public ResponseEntity<ResultDto<Void>> communityUpdate(@RequestHeader("Authorization") String accessToken,
                                                           @PathVariable(value = "communityId") Long communityId,
                                                           @RequestPart(value = "imageList") List<MultipartFile> multipartFileList,
                                                           @RequestPart(value = "communityUpdateRequestDto") CommunityUpdateRequestDto communityUpdateRequestDto) {

        CommonResponseDto<Object> communityUpdate = communityService.communityUpdate(accessToken, communityId, multipartFileList, communityUpdateRequestDto);
        ResultDto<Void> result = ResultDto.in(communityUpdate.getStatus(), communityUpdate.getMessage());

        return ResponseEntity.status(communityUpdate.getHttpStatus()).body(result);
    }

    // 커뮤니티 참가 API
    @PostMapping("/community/{communityId}")
    public ResponseEntity<ResultDto<Void>> communitySignup(@RequestHeader("Authorization") String accessToken,
                                                           @PathVariable(value = "communityId") Long communityId) {
        CommonResponseDto<Object> communitySignup = communityService.communitySignup(accessToken, communityId);
        ResultDto<Void> result = ResultDto.in(communitySignup.getStatus(), communitySignup.getMessage());

        return ResponseEntity.status(communitySignup.getHttpStatus()).body(result);
    }
}
