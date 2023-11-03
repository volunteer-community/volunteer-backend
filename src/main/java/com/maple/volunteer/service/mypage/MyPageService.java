package com.maple.volunteer.service.mypage;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.dto.community.CommunityListResponseDto;
import com.maple.volunteer.dto.community.CommunityResponseDto;
import com.maple.volunteer.dto.mypage.MyPageResponseDto;
import com.maple.volunteer.exception.BadRequestException;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.comment.CommentRepository;
import com.maple.volunteer.repository.community.CommunityRepository;
import com.maple.volunteer.repository.communityuser.CommunityUserRepository;
import com.maple.volunteer.repository.heart.HeartRepository;
import com.maple.volunteer.repository.poster.PosterRepository;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final CommunityUserRepository communityUserRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;
    private final HeartRepository heartRepository;
    private final PosterRepository posterRepository;
    private final CommentRepository commentRepository;


    // 내가 만든 커뮤니티 리스트
    public CommonResponseDto<Object> myCommunityCreateList(String accessToken, int page, int size, String sortBy) {

        String nickName = findUserNickname(accessToken);

        // 페이지, 요소 개수, 정렬 설정
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(sortBy)
                                                                  .descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityRepository.findCommunityListByAuthor(nickName, pageable);

        if (data.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMUNITY_NOT_FOUND);
        }

        // 커뮤니티 리스트 가져오기
        List<CommunityResponseDto> myCommunityCreateList = data.getContent();

        // 페이지네이션 설정
        PaginationDto paginationDto = PaginationDto.builder()
                                                   .totalPages(data.getTotalPages())
                                                   .totalElements(data.getTotalElements())
                                                   .pageNo(data.getNumber())
                                                   .isLastPage(data.isLast())
                                                   .build();

        // 페이지네이션을 포함한 커뮤니티 리스트 반환
        CommunityListResponseDto myCommunityCreateListResponseDto = CommunityListResponseDto.builder()
                                                                                            .communityList(myCommunityCreateList)
                                                                                            .paginationDto(paginationDto)
                                                                                            .build();

        return commonService.successResponse(SuccessCode.MY_COMMUNITY_CREATED_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, myCommunityCreateListResponseDto);
    }


    // 내가 가입한 커뮤니티 리스트
    public CommonResponseDto<Object> myCommunitySignList(String accessToken, int page, int size, String sortBy) {

        // UserId 가져오기
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));

        // 페이지, 요소 개수, 정렬 설정
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(sortBy)
                                                                  .descending());

        // 페이지로 값 가져오기
        Page<CommunityResponseDto> data = communityUserRepository.myCommunitySignList(userId, pageable);

        if (data.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMUNITY_NOT_FOUND);
        }

        // 커뮤니티 리스트 가져오기
        List<CommunityResponseDto> myCommunitySignList = data.getContent();

        // 페이지네이션 설정
        PaginationDto paginationDto = PaginationDto.builder()
                                                   .totalPages(data.getTotalPages())
                                                   .totalElements(data.getTotalElements())
                                                   .pageNo(data.getNumber())
                                                   .isLastPage(data.isLast())
                                                   .build();

        // 페이지네이션을 포함한 커뮤니티 리스트 반환
        CommunityListResponseDto myCommunitySignListResponseDto = CommunityListResponseDto.builder()
                                                                                          .communityList(myCommunitySignList)
                                                                                          .paginationDto(paginationDto)
                                                                                          .build();

        return commonService.successResponse(SuccessCode.MY_COMMUNITY_CREATED_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, myCommunitySignListResponseDto);
    }


    // 유저 닉네임 가져오기
    private String findUserNickname(String accessToken) {
        // UserId 가져오기
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));

        // 유저 가져오기
        User user = userRepository.findById(userId)
                                  // 유저가 없다면 오류 반환
                                  .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // 유저 닉네임 가져오기
        return user.getNickname();
    }

    // 내가 가입한 커뮤니티 개수
    private Integer findCommunityUserCount(Long userId) {
        if(communityUserRepository.myCommunitySignNumber(userId) !=null) {
            return communityUserRepository.myCommunitySignNumber(userId);
        }else {
            return 0;
        }
    }


    // 유저가 좋아요 한 게시글 개수
    private Integer numberOfPosterthatILike(Long userId) {


        List<CommunityUser> AllCommunityUser = communityUserRepository.findCommunityUserByUserId(userId);

        int totalLikedCount = 0;
        for (CommunityUser eachCommunityUser : AllCommunityUser) {
            Long communityUserId = eachCommunityUser.getId();
            Integer likedCount = heartRepository.countByCommunityUserId(communityUserId);

            totalLikedCount += likedCount;
        }

        return totalLikedCount;
    }

    // 좋아요 받은 게시글 개수
/*    private Integer numberOfLikedPoster(Long userId) {

        List<CommunityUser> AllCommunityUser = communityUserRepository.findCommunityUserByUserId(userId);

        int totalPosterLikedCount = 0;
        for (CommunityUser eachCommunityUser : AllCommunityUser) {
            Long communityUserId = eachCommunityUser.getId();
            List<Poster> AllPoster = posterRepository.findByCommunityUserId(communityUserId);
            for (Poster eachPoster : AllPoster) {
                Long posterId = eachPoster.getId();
                Integer posterLikedCount = posterRepository.countByPosterId(posterId);
                totalPosterLikedCount += posterLikedCount;
            }
        }
        return totalPosterLikedCount;
    }*/

    // 좋아요 받은 게시글 개수
    private Integer numberOfLikedPoster(Long userId) {
        // 게시글이 있을때 /없을 때
//        Boolean posterStatus =  posterRepository.exitsByIdAndIsDelete(userId);
        if(posterRepository.numberOfLikedPoster(userId) != null) {
            return posterRepository.numberOfLikedPoster(userId);
        }else {
            return 0;
        }
    }



    // 내가 쓴 댓글 개수

    private Integer findCommentCount(Long userId) {

        List<CommunityUser> AllCommunityUser = communityUserRepository.findCommunityUserByUserId(userId);

        if(communityUserRepository.findCommunityUserByUserId(userId) !=null) {
            int totalCommnetCount = 0;
            for (CommunityUser eachCommunityUser : AllCommunityUser) {
                Long communityUserId = eachCommunityUser.getId();
                Integer commentCount = commentRepository.countByCommunityUserId(communityUserId);
                totalCommnetCount += commentCount;
            }

            return totalCommnetCount;
        }else {
            return 0;
        }
    }

    // 마이페이지 INFO
    public CommonResponseDto<Object> getMyInfo(String accessToken) {
        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                                                               .communityUserCount(findCommunityUserCount(userId))
                                                               .countOfPosterLike(numberOfPosterthatILike(userId))
                                                               .countOfLikedPoster(numberOfLikedPoster(userId))
                                                               .commentCount(findCommentCount(userId))
                                                               .build();

        return commonService.successResponse(SuccessCode.MY_PAGE_INFO_SUCCESS.getDescription(), HttpStatus.OK, myPageResponseDto);
    }
}
