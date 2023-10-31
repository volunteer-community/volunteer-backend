package com.maple.volunteer.controller.mypage;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.community.CommunityResponseDto;
import com.maple.volunteer.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maple")
public class MyPageController {

    private final MyPageService myPageService;

    // 내가 만든 커뮤니티 API
    @GetMapping("/mypage/community")
    public ResponseEntity<ResultDto<CommunityResponseDto>> myCommunityCreateList(@RequestHeader("Authorization") String accessToken,
                                                                 @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                                 @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                 @RequestParam(value = "sortBy", defaultValue = "updatedAt", required = false) String sortBy) {
        CommonResponseDto<Object> myCommunityCreateList = myPageService.myCommunityCreateList(accessToken, page, size, sortBy);
        ResultDto<CommunityResponseDto> result = ResultDto.in(myCommunityCreateList.getStatus(), myCommunityCreateList.getMessage());
        result.setData((CommunityResponseDto) myCommunityCreateList.getData());

        return ResponseEntity.status(myCommunityCreateList.getHttpStatus()).body(result);
    }

    // 내가 가입한 커뮤니티 API
    @GetMapping("/mypage/community/sign")
    public ResponseEntity<ResultDto<CommunityResponseDto>> myCommunitySignList(@RequestHeader("Authorization") String accessToken,
                                                               @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                               @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                               @RequestParam(value = "sortBy", defaultValue = "updatedAt", required = false) String sortBy) {
        CommonResponseDto<Object> myCommunitySignList = myPageService.myCommunitySignList(accessToken, page, size, sortBy);
        ResultDto<CommunityResponseDto> result = ResultDto.in(myCommunitySignList.getStatus(), myCommunitySignList.getMessage());
        result.setData((CommunityResponseDto) myCommunitySignList.getData());

        return ResponseEntity.status(myCommunitySignList.getHttpStatus()).body(result);
    }

}
