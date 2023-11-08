package com.maple.volunteer.service.mypage;

import com.maple.volunteer.domain.community.Community;
import com.maple.volunteer.domain.communityimg.CommunityImg;
import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.heart.Heart;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.domain.posterimg.PosterImg;
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
import com.maple.volunteer.repository.communityimg.CommunityImgRepository;
import com.maple.volunteer.repository.communityuser.CommunityUserRepository;
import com.maple.volunteer.repository.heart.HeartRepository;
import com.maple.volunteer.repository.login.LoginRepository;
import com.maple.volunteer.repository.poster.PosterRepository;
import com.maple.volunteer.repository.posterimg.PosterImgRepository;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.service.s3upload.S3UploadService;
import com.maple.volunteer.type.CommunityStatus;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final CommunityUserRepository communityUserRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final HeartRepository heartRepository;
    private final PosterRepository posterRepository;
    private final CommentRepository commentRepository;
    private final PosterImgRepository posterImgRepository;
    private final CommunityImgRepository communityImgRepository;
    private final CommonService commonService;
    private final S3UploadService s3UploadService;
    private final JwtUtil jwtUtil;
    private final LoginRepository loginRepository;


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

        return commonService.successResponse(SuccessCode.MY_COMMUNITY_SIGN_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, myCommunitySignListResponseDto);
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

    // 회원 탈퇴
    @Transactional
    public CommonResponseDto<Object> withdraw(String accessToken) {

        Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
        User user = userRepository.findByIdAndIsDelete(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        String author = user.getNickname();

        // 내가 작성한 커뮤니티 리스트
        List<Community> communityList = communityRepository.findCommunitiesByAuthor(author);

        // 내가 만든 커뮤니티가 있다면
        if(!communityList.isEmpty()){

            // 커뮤니티 삭제 , 커뮤니티 이미지 삭제, 해당 게시글 , 좋아요 , 댓글 삭제 , 커뮤니티 가입된 유저 탈퇴
            // 해당 커뮤니티에 속하는 게시글, 댓글, 커뮤니티 유저, 좋아요 모두 삭제
            for(Community eachCommunity : communityList){
                Long communityId = eachCommunity.getId();

                // 게시글 가져오기
                Boolean existsCommunityId = posterRepository.existsCommunityId(communityId);

                // 게시글이 없우경는
                if (!existsCommunityId) {

                    communityUserRepository.CommunityUserDelete(communityId, true);

                    // 커뮤니티 이미지 삭제
                    // 이미지 url 값만 가져오기
                    List<CommunityImg> communityImgPathList = communityImgRepository.findDeletedCommunityImgPathList(communityId);

                    // url 값 삭제
                    for (CommunityImg communityImgPath : communityImgPathList) {
                        String imgPath = communityImgPath.getImagePath();

                        // s3 이미지 삭제
                        s3UploadService.deleteCommunityImg(imgPath);

                        // DB isDelete = true 로 변경
                        communityImgRepository.deleteByCommunityImgId(communityImgPath.getId(), true);
                    }

                    // isDelete 값을 true로 변경
                    communityRepository.deleteCommunityId(communityId, true, CommunityStatus.COMMUNITY_RECRUITMENT_END.getDescription());
                }

                // 게시글이 있는 경우
                commentRepository.commentDeleteByCommunityId(communityId, true);
                heartRepository.updateStatusByCommunityId(communityId, false);
                posterRepository.posterDeleteByCommunityId(communityId, true);
                communityUserRepository.CommunityUserDelete(communityId, true);

                // 게시글 이미지 삭제
                // 이미지 삭제 (s3삭제)
                List<PosterImg> posterImg = posterImgRepository.findByPosterListByCommunityId(communityId);
                for(PosterImg eachPosterImg : posterImg){
                    String posterImgUrl = eachPosterImg.getImagePath();
                    s3UploadService.deletePosterImg(posterImgUrl);

                    // DB isDelete = true 로 변경
                    Long posterImgId= eachPosterImg.getId();
                    posterImgRepository.deleteByPosterImgId(posterImgId,true);
                }


                // 커뮤니티 이미지 삭제
                // 이미지 url 값만 가져오기
                List<CommunityImg> communityImgPathList = communityImgRepository.findDeletedCommunityImgPathList(communityId);

                // url 값 삭제
                for (CommunityImg communityImgPath : communityImgPathList) {
                    String imgPath = communityImgPath.getImagePath();

                    // s3 이미지 삭제
                    s3UploadService.deleteCommunityImg(imgPath);

                    // DB isDelete = true 로 변경
                    communityImgRepository.deleteByCommunityImgId(communityImgPath.getId(),true);
                }

                // 커뮤니티 삭제
                communityRepository.deleteCommunityId(communityId,true,CommunityStatus.COMMUNITY_RECRUITMENT_END.getDescription());

            }
        }

        // 다른 사람의 커뮤니티
        //userid 로 communityId를 불러와서 조회하고 탈퇴
        List<CommunityUser> communityUserList= communityUserRepository.findCommunityUserByUserId(userId);
        // if 참가한 커뮤니티 o
        if (!communityUserList.isEmpty()){
            for(CommunityUser eachCommunityUser : communityUserList){

                Long communityUserId = eachCommunityUser.getId();
                Community community = eachCommunityUser.getCommunity();
                Long communityId = community.getId();

                // 탈퇴하는 유저ID가 작성한 게시글 (게시글 좋아요, 좋아요 개수 0, 댓글 삭제) 삭제

                //1. 유저ID에 해당되는 게시글 리스트 가져오기
                List<Poster> posterList = posterRepository.findByPosterListCommunityUserId(communityUserId);
                if(posterList.isEmpty()) {

                    // 해당 레코드의 isWithdraw를 true로 변환
                    communityUserRepository.communityWithdraw(communityUserId,true);

                    // 참가 인원 감소
                    communityRepository.participantDecrease(communityId,CommunityStatus.COMMUNITY_RECRUITMENT_ING.getDescription());

                    // 내가 쓴 게시글이 아닌 타인의 글
                    // 탈퇴하는 유저ID가 누른 타인의 게시글에서 좋아요 개수 -1 시키기 , 좋아요 false 시키기
                    List<Heart> heartListByPosterId = heartRepository.findHeartListCommunityUserId(communityUserId);

                    for (Heart eachHeartPosterId : heartListByPosterId) {

                        Long heartId = eachHeartPosterId.getId();
                        Long posterId = eachHeartPosterId.getPoster().getId();

                        heartRepository.updateStatus(heartId, false);
                        posterRepository.updateHeartCountDecrease(posterId);
                    }

                    // 타인의 글에서 댓글 삭제
                    commentRepository.commentDeleteByCommunityUserId(communityUserId, true);

                    loginRepository.deleteByUserId(userId);
                    userRepository.updateUserIsDelete(userId);

                    return commonService.successResponse(SuccessCode.MY_PAGE_USER_WITHDRAWAL_SUCCESS.getDescription(), HttpStatus.OK, null);
                }

                // 해당 레코드의 isWithdraw를 true로 변환
                communityUserRepository.communityWithdraw(communityUserId,true);

                // 참가 인원 감소
                communityRepository.participantDecrease(communityId, CommunityStatus.COMMUNITY_RECRUITMENT_ING.getDescription());

                for (Poster eachPoster : posterList) {
                    Long posterId = eachPoster.getId();

                    //게시글 Id에 해당되는 heartCount = 0 으로 변경
                    posterRepository.updateHeartCountZero(posterId);

                    //게시글 ID에 해당되는 댓글 삭제(isDeleted = true)
                    commentRepository.commentDeleteByPosterId(posterId);

                    // 이미지 삭제 (s3삭제)
                    PosterImg posterImg = posterImgRepository.findByPosterId(posterId);
                    String posterImgUrl = posterImg.getImagePath();
                    s3UploadService.deletePosterImg(posterImgUrl);

                    // DB isDelete = true 로 변경
                    Long posterImgId = posterImg.getId();
                    posterImgRepository.deleteByPosterImgId(posterImgId, true);

                    //유저 ID에 해당하는 좋아요 리스트 가져오기
                    List<Heart> heartList = heartRepository.findHeartListPosterId(posterId);

                    for (Heart eachHeart : heartList) {
                        Long heartId = eachHeart.getId();

                        //가져온 좋아요를 false로 바꿔주기(좋아요 삭제)
                        heartRepository.updateStatus(heartId, false);

                    }

                }

                // 내가 쓴 게시글이 아닌 타인의 글
                // 탈퇴하는 유저ID가 누른 타인의 게시글에서 좋아요 개수 -1 시키기 , 좋아요 false 시키기

                List<Heart> heartListByPosterId = heartRepository.findHeartListCommunityUserId(communityUserId);

                for (Heart eachHeartPosterId : heartListByPosterId) {

                    Long heartId = eachHeartPosterId.getId();
                    Long posterId = eachHeartPosterId.getPoster().getId();

                    heartRepository.updateStatus(heartId, false);
                    posterRepository.updateHeartCountDecrease(posterId);
                }

                // 유저Id에 해당되는 게시글 삭제/댓글 삭제

                posterRepository.posterDeleteByCommunityUserId(communityUserId, true);
                commentRepository.commentDeleteByCommunityUserId(communityUserId, true);
            }
        }

        loginRepository.deleteByUserId(userId);
        userRepository.updateUserIsDelete(userId);

        return commonService.successResponse(SuccessCode.MY_PAGE_USER_WITHDRAWAL_SUCCESS.getDescription(), HttpStatus.OK, null);
    }


}
