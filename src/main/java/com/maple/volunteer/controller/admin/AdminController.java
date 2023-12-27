package com.maple.volunteer.controller.admin;

import com.maple.volunteer.dto.admin.AllUserListDto;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.user.UserDto;
import com.maple.volunteer.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maple/admin")
public class AdminController {

    private final AdminService adminService;

    // 전체 회원 조회 (탈퇴 포함)
    @Secured("ROLE_ADMIN")
    @GetMapping("/allUser")
    public ResponseEntity<ResultDto<AllUserListDto>> allUserInquiry(@RequestHeader("Authorization") String accesToken,
                                                                    @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                    @RequestParam(value = "sortBy", defaultValue = "updatedAt", required = false) String sortBy) {

        CommonResponseDto<Object> allUserInquiry = adminService.allUserInquiry(page, size, sortBy);
        ResultDto<AllUserListDto> result = ResultDto.in(allUserInquiry.getStatus(), allUserInquiry.getMessage());
        result.setData((AllUserListDto) allUserInquiry.getData());

        return ResponseEntity.status(allUserInquiry.getHttpStatus()).body(result);
    }

    // 닉네임으로 회원 조회 (탈퇴 포함)
    @Secured("ROLE_ADMIN")
    @GetMapping("/user")
    public ResponseEntity<ResultDto<UserDto>> userInquiry(@RequestHeader("Authorization") String accesToken,
                                                          @RequestParam("nickname") String nickname) {

        CommonResponseDto<Object> userInquiry = adminService.userInquiryByNickname(nickname);
        ResultDto<UserDto> result = ResultDto.in(userInquiry.getStatus(), userInquiry.getMessage());
        result.setData((UserDto) userInquiry.getData());

        return ResponseEntity.status(userInquiry.getHttpStatus()).body(result);
    }
}
