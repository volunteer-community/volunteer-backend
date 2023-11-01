package com.maple.volunteer.service.user;

import com.maple.volunteer.domain.login.Login;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.admin.AllUserListDto;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.dto.user.*;
import com.maple.volunteer.repository.login.LoginRepository;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import com.maple.volunteer.security.jwt.dto.GeneratedToken;
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
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;


    // 로그인
    public CommonResponseDto<Object> login(String email) {
        // email로 User(false) get
        User user = userRepository.findActiveUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Long userId = user.getId();

        // accessToken, refreshToken 발행
        GeneratedToken token = jwtUtil.generateToken(userId);

        // 기존 refreshToken 변경
        Login login = user.getLogin();
        loginRepository.updateRefreshTokenById(login.getId(), token.getRefreshToken());

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .build();

        return commonService.successResponse(SuccessCode.USER_LOGIN_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
    }

    // 로그아웃
    public CommonResponseDto<Object> logout(String accessToken) {

        // 토큰 통해 userId get -> 유저 조회
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // refreshToken -> null 변경
        Login login = user.getLogin();
        loginRepository.updateRefreshTokenById(login.getId(), null);

        return commonService.successResponse(SuccessCode.USER_LOGOUT_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 토큰 갱신
    public CommonResponseDto<Object> renewToken(String refreshToken) {

        // 토큰 통해 유저 get
        Login login = loginRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TOKEN_NOT_FOUND));

        User user = login.getUser();
        Long userid = user.getId();

        // accessToken, refreshToken 재발급
        GeneratedToken token = jwtUtil.generateToken(userid);

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .build();


        return commonService.successResponse(SuccessCode.USER_RENEW_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
    }

    // 회원가입
    public CommonResponseDto<Object> signup(SignupDto signupDto) {
        if(!findByPhoneNumber(signupDto.getPhoneNumber())){
            if(!findByNickName(signupDto.getName())){
                User user = User.builder()
                        .phoneNumber(signupDto.getPhoneNumber())
                        .name(signupDto.getName())
                        .role(signupDto.getRole())
                        .email(signupDto.getEmail())
                        .profileImg(signupDto.getPicture())
                        .nickname(signupDto.getNickname())
                        .build();
                userRepository.save(user);

                // email로 User(false) get
                User user2 = userRepository.findActiveUserByEmail(signupDto.getEmail())
                        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
                Long userId = user2.getId();

                GeneratedToken token = jwtUtil.generateToken(userId);

                Login login = Login.builder()
                        .user(user2)
                        .provider(signupDto.getProvider())
                        .refreshToken(token.getRefreshToken())
                        .build();

                loginRepository.save(login);

                TokenDto tokenDto = TokenDto.builder()
                        .accessToken(token.getAccessToken())
                        .refreshToken(token.getRefreshToken())
                        .accessTokenExpireTime(token.getAccessTokenExpireTime())
                        .build();

                return commonService.successResponse(SuccessCode.USER_LOGIN_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);}
            else{
                //이미 가입한 닉네임
                return commonService.errorResponse(ErrorCode.EXISTED_NICKNAME.getDescription(), HttpStatus.BAD_REQUEST, null);
            }
        }else {
            //이미 가입한 핸드폰 번호
            return commonService.errorResponse(ErrorCode.EXISTED_PHONE_NUMBER.getDescription(), HttpStatus.BAD_REQUEST, null);
        }
    }

    private boolean findByNickName(String nickname) {
        Optional<User> userOptional = userRepository.findNickname(nickname);
        return userOptional.isEmpty();
    }

    private boolean findByPhoneNumber(String phoneNumber) {
        Optional<User> userOptional = userRepository.findPhone(phoneNumber);
        return userOptional.isEmpty();
    }

    public CommonResponseDto<Object> addinfo(String email, String picture) {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail(email);
        newUserDto.setPicture(picture);
        return commonService.successResponse(SuccessCode.NEW_USER_SUCCESS.getDescription(), HttpStatus.OK, newUserDto);
    }

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

    public CommonResponseDto<Object> nicknameCheck(CheckDto checkDto) {
        Optional<User> userOptional = userRepository.findNickname(checkDto.getCheck());
        if(userOptional.isEmpty()){
            checkDto.setExist(false);
            return commonService.successResponse(SuccessCode.NICKNAME_AVAILABLE.getDescription(), HttpStatus.OK, checkDto);
        }else {
            checkDto.setExist(true);
            return commonService.successResponse(SuccessCode.NICKNAME_NOT_AVAILABLE.getDescription(), HttpStatus.OK, checkDto);
        }
    }

    public CommonResponseDto<Object> phoneCheck(CheckDto phoneCheckDto) {
        Optional<User> userOptional = userRepository.findPhone(phoneCheckDto.getCheck());
        if (userOptional.isEmpty()){
            phoneCheckDto.setExist(false);
            return commonService.successResponse(SuccessCode.PHONE_NUMBER_AVAILABLE.getDescription(), HttpStatus.OK,phoneCheckDto);
        }else {
            phoneCheckDto.setExist(true);
            return commonService.successResponse(SuccessCode.PHONE_NUMBER_NOT_AVAILABLE.getDescription(), HttpStatus.OK,phoneCheckDto);
        }
    }

    public CommonResponseDto<Object> viewUserInfo(String accessToken) {
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            ViewUserDto viewUserDto = new ViewUserDto();
            viewUserDto.setNickname(user.getNickname());
            viewUserDto.setName(user.getName());
            viewUserDto.setPhone(user.getPhoneNumber());
            viewUserDto.setPicture(user.getProfileImg());
            viewUserDto.setEmail(user.getEmail());
            return commonService.successResponse(SuccessCode.VIEW_USERINFO_SUCCESS.getDescription(), HttpStatus.OK, viewUserDto);
        }else{
            return commonService.errorResponse(ErrorCode.INVALID_USER_REQUEST.getDescription(), HttpStatus.BAD_REQUEST, null);
        }
    }

    public CommonResponseDto<Object> modUserInfo(String accessToken, ViewUserDto viewUserDto) {
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        userRepository.updateUserInfo(viewUserDto.getPhone(), viewUserDto.getNickname(), userId);
        return commonService.successResponse(SuccessCode.MODIFY_USERINFO_SUCCESS.getDescription(), HttpStatus.OK,null);
    }
}
