package com.maple.volunteer.service.admin;

import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.admin.AllUserListDto;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.dto.user.UserDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CommonService commonService;

    // 유저 조회 (true, false 모두)
    public CommonResponseDto<Object> allUserInquiry (int page, int size, String sortBy ) {

        // 페이지, 요소 개수, 정렬
        PageRequest pageable = PageRequest.of(page -1, size, Sort.by(sortBy).descending());

        // 페이지 값 받아오기
        Page<User> data = userRepository.findAll(pageable);
        if (data.isEmpty()) throw new NotFoundException(ErrorCode.USER_NOT_FOUND);

        // userDtoList 생성
        List<User> userList = data.getContent();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user: userList) {
            UserDto userDto = UserDto.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .phoneNumber(user.getPhoneNumber())
                    .profileImg(user.getProfileImg())
                    .provider(user.getProvider())
                    .build();
            userDtoList.add(userDto);
        }

        // pagination 설정
        PaginationDto paginationDto = PaginationDto.builder()
                .totalPages(data.getTotalPages())
                .totalElements(data.getTotalElements())
                .pageNo(data.getNumber())
                .isLastPage(data.isLast())
                .build();

        // AllUserListDto 반환
        AllUserListDto allUserListDto = AllUserListDto.builder()
                .userList(userDtoList)
                .paginationDto(paginationDto)
                .build();

        return commonService.successResponse(SuccessCode.ALL_USER_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, allUserListDto);
    }

    // 닉네임으로 회원 조회 (true, false 모두)
    public CommonResponseDto<Object> userInquiryByNickname (String nickname) {

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        UserDto userDto = UserDto.builder()
                .name(user.getName())
                .provider(user.getProvider())
                .phoneNumber(user.getPhoneNumber())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .isDeleted(user.isDelete())
                .build();

        return commonService.successResponse(SuccessCode.VIEW_USERINFO_SUCCESS.getDescription(), HttpStatus.OK, userDto);
    }
}
