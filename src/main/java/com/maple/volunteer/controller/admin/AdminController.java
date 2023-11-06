package com.maple.volunteer.controller.admin;

import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.admin.AllUserListDto;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.community.CommunityListResponseDto;
import com.maple.volunteer.dto.user.UserDto;
import com.maple.volunteer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maple/admin")
public class AdminController {

    private final UserService userService;

    // 전체 회원 조회 (탈퇴 포함)
    @GetMapping("/allUser")
    public ResponseEntity<ResultDto<AllUserListDto>> allUserInquiry(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                                      @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                      @RequestParam(value = "sortBy", defaultValue = "updatedAt", required = false) String sortBy) {

        CommonResponseDto<Object> allUserInquiry = userService.allUserInquiry(page, size, sortBy);
        ResultDto<AllUserListDto> result = ResultDto.in(allUserInquiry.getStatus(), allUserInquiry.getMessage());
        result.setData((AllUserListDto) allUserInquiry.getData());

        return ResponseEntity.status(allUserInquiry.getHttpStatus()).body(result);
    }

    // 닉네임으로 회원 조회 (탈퇴 포함)
    @GetMapping("/user")
    public ResponseEntity<ResultDto<UserDto>> userInquiry(@RequestParam("nickname") String nickname) {

        CommonResponseDto<Object> userInquiry = userService.userInquiryByNickname(nickname);
        ResultDto<UserDto> result = ResultDto.in(userInquiry.getStatus(), userInquiry.getMessage());
        result.setData((UserDto) userInquiry.getData());

        return ResponseEntity.status(userInquiry.getHttpStatus()).body(result);
    }
}
