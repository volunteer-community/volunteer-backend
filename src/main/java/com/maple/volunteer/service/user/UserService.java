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
import com.maple.volunteer.type.Role;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;


    // 로그인
    @Transactional
    public TokenDto login(String email, String role, String provider, String profileImg) {
        // email, provider로 User(false) get
        User user = userRepository.findActiveUserByEmailAndProvider(email, provider)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!user.getProfileImg().equals(profileImg)) {
            user.updateProfileImg(profileImg);
        }

        Long userId = user.getId();

        // accessToken, refreshToken 발행
        GeneratedToken token = jwtUtil.generateToken(userId, role);

        // 기존 refreshToken 변경
        Login login = user.getLogin();
        loginRepository.updateRefreshTokenById(login.getId(), token.getRefreshToken());

        return TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .refreshTokenExpireTime(token.getRefreshTokenExpireTime())
                .build();
    }

    // 로그아웃
    @Transactional
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
    @Transactional
    public CommonResponseDto<Object> renewToken(String refreshToken) {

        // 토큰 통해 유저 get
        Login login = loginRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TOKEN_NOT_FOUND));

        User user = login.getUser();
        Long userid = user.getId();
        String userRole = user.getRole().getKey();

        // accessToken, refreshToken 재발급
        GeneratedToken token = jwtUtil.generateToken(userid, userRole);

        login.updateRefreshToken(token.getRefreshToken());

//        TokenDto tokenDto = TokenDto.builder()
//                .accessToken(token.getAccessToken())
//                .refreshToken(token.getRefreshToken())
//                .accessTokenExpireTime(token.getAccessTokenExpireTime())
//                .build();


        return commonService.successResponse(SuccessCode.USER_RENEW_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 회원가입
    public CommonResponseDto<Object> signup(SignupDto signupDto) {
        List<User> userList = userRepository.findActiveUserByEmail2(signupDto.getEmail(), signupDto.getProvider());
        if(userList.size()==1)return commonService.errorResponse(ErrorCode.EXIST_USER_EMAIL.getDescription(), HttpStatus.BAD_REQUEST,null);
        else if(userList.size()>1) return commonService.errorResponse(ErrorCode.MULTIPLE_USER_FOUND.getDescription(), HttpStatus.BAD_REQUEST,null);
        //email의 active유저값이 없을때
        if(findByPhoneNumber(signupDto.getPhoneNumber())){
            if(findByNickName(signupDto.getNickname())){
                // email로 User(false) get
                    User user = User.builder()
                            .phoneNumber(signupDto.getPhoneNumber())
                            .name(signupDto.getName())
                            .role(signupDto.getRole())
                            .email(signupDto.getEmail())
                            .profileImg(signupDto.getPicture())
                            .nickname(signupDto.getNickname())
                            .provider(signupDto.getProvider())
                            .isDeleted(false)
                            .build();
                    userRepository.save(user);

                    //회원가입한 사람을 로그인시키기
                List<User> loginedUserList = userRepository.findActiveUserByEmail2(signupDto.getEmail(),signupDto.getProvider());
                if(loginedUserList.isEmpty())return commonService.errorResponse(ErrorCode.USER_NOT_FOUND.getDescription(), HttpStatus.BAD_REQUEST,null);
                else if(loginedUserList.size()>1) return commonService.errorResponse(ErrorCode.MULTIPLE_USER_FOUND.getDescription(), HttpStatus.BAD_REQUEST,null);

                    User loginUser = loginedUserList.get(0);
                    Long userId = loginUser.getId();
                    String userRole = loginUser.getRole().getKey();

                    GeneratedToken token = jwtUtil.generateToken(userId, userRole);

                    Login login = Login.builder()
                            .user(loginUser)
                            .refreshToken(token.getRefreshToken())
                            .build();

                    loginRepository.save(login);

//                    TokenDto tokenDto = TokenDto.builder()
//                            .accessToken(token.getAccessToken())
//                            .refreshToken(token.getRefreshToken())
//                            .accessTokenExpireTime(token.getAccessTokenExpireTime())
//                            .refreshTokenExpireTime(token.getRefreshTokenExpireTime())
//                            .build();

                    return commonService.successResponse(SuccessCode.USER_LOGIN_SUCCESS.getDescription(), HttpStatus.OK, null);
            }
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

//    public CommonResponseDto<Object> addinfo(String email, String picture, String role, String name, String provider) {
//        NewUserDto newUserDto = new NewUserDto();
//        newUserDto.setEmail(email);
//        newUserDto.setPicture(picture);
//        newUserDto.setRole(role);
//        newUserDto.setName(name);
//        newUserDto.setProvider(provider);
//        return commonService.successResponse(SuccessCode.NEW_USER_SUCCESS.getDescription(), HttpStatus.OK, newUserDto);
//    }

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
                .isDeleted(user.isDeleted())
                .build();

        return commonService.successResponse(SuccessCode.VIEW_USERINFO_SUCCESS.getDescription(), HttpStatus.OK, userDto);
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
