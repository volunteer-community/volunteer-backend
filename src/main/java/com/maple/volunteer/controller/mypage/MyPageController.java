package com.maple.volunteer.controller.mypage;

import com.maple.volunteer.dto.comment.CommentListResponseDto;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.community.CommunityListResponseDto;
import com.maple.volunteer.dto.community.CommunityResponseDto;
import com.maple.volunteer.dto.mypage.MyPageResponseDto;
import com.maple.volunteer.dto.user.ViewUserDto;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import com.maple.volunteer.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maple")
public class MyPageController {

    private final MyPageService myPageService;
    private final JwtUtil jwtUtil;

    // 내가 만든 커뮤니티 API
    @GetMapping("/mypage/community")
    public ResponseEntity<ResultDto<CommunityListResponseDto>> myCommunityCreateList(@RequestHeader("Authorization") String accessToken,
                                                                 @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                                 @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                 @RequestParam(value = "sortBy", defaultValue = "updatedAt", required = false) String sortBy) {
        CommonResponseDto<Object> myCommunityCreateList = myPageService.myCommunityCreateList(accessToken, page, size, sortBy);
        ResultDto<CommunityListResponseDto> result = ResultDto.in(myCommunityCreateList.getStatus(), myCommunityCreateList.getMessage());
        result.setData((CommunityListResponseDto) myCommunityCreateList.getData());

        return ResponseEntity.status(myCommunityCreateList.getHttpStatus()).body(result);
    }

    // 내가 가입한 커뮤니티 API
    @GetMapping("/mypage/community/sign")
    public ResponseEntity<ResultDto<CommunityListResponseDto>> myCommunitySignList(@RequestHeader("Authorization") String accessToken,
                                                               @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                               @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                               @RequestParam(value = "sortBy", defaultValue = "updatedAt", required = false) String sortBy) {
        CommonResponseDto<Object> myCommunitySignList = myPageService.myCommunitySignList(accessToken, page, size, sortBy);
        ResultDto<CommunityListResponseDto> result = ResultDto.in(myCommunitySignList.getStatus(), myCommunitySignList.getMessage());
        result.setData((CommunityListResponseDto) myCommunitySignList.getData());

        return ResponseEntity.status(myCommunitySignList.getHttpStatus()).body(result);
    }


    // 마이페이지 나의활동
    @GetMapping("/mypage/myinfo")
    public ResponseEntity<ResultDto<MyPageResponseDto>> getMyInfo(@RequestHeader("Authorization") String accessToken){

        CommonResponseDto<Object> allMyInfo = myPageService.getMyInfo(accessToken);
        ResultDto<MyPageResponseDto> result = ResultDto.in(allMyInfo.getStatus(), allMyInfo.getMessage());
        result.setData((MyPageResponseDto) allMyInfo.getData());

        return ResponseEntity.status(allMyInfo.getHttpStatus()).body(result);
    }

    @PostMapping("/mypage/withdrawal")
    public ResponseEntity<ResultDto<Void>> withdraw(@RequestHeader("Authorization") String accessToken){

        CommonResponseDto<Object> withdraw = myPageService.withdraw(accessToken);
        ResultDto<Void> result = ResultDto.in(withdraw.getStatus(),withdraw.getMessage());

        return ResponseEntity.status(withdraw.getHttpStatus()).body(result);
    }

    // 유저 정보 수정 페이지에 정보 넘기기
    @PostMapping("/mypage/viewUserInfo")
    public ResponseEntity<ResultDto<ViewUserDto>> viewUserInfo(@RequestHeader("Authorization") String accessToken){
        CommonResponseDto<Object> commonResponseDto = myPageService.viewUserInfo(accessToken);
        ResultDto<ViewUserDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((ViewUserDto) commonResponseDto.getData());
        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }

    // 유저 정보 수정하기
    @GetMapping("/mypage/modUserInfo")
    public ResponseEntity<ResultDto<Void>> modUserInfo(@RequestHeader("Authorization") String accessToken,
                                                       @RequestBody ViewUserDto viewUserDto){
        CommonResponseDto<Object> commonResponseDto = myPageService.modUserInfo(accessToken, viewUserDto);
        ResultDto<Void> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }
}
