package com.maple.volunteer.service.comment;

import com.maple.volunteer.domain.comment.Comment;
import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.domain.poster.Poster;
import com.maple.volunteer.dto.comment.CommentListResponseDto;
import com.maple.volunteer.dto.comment.CommentRequestDto;
import com.maple.volunteer.dto.comment.CommentResponseDto;
import com.maple.volunteer.dto.comment.CommentUpdateDto;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.comment.CommentRepository;
import com.maple.volunteer.repository.communityuser.CommunityUserRepository;
import com.maple.volunteer.repository.poster.PosterRepository;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommonService commonService;
    private final CommunityUserRepository communityUserRepository;
    private final PosterRepository posterRepository;

    // 댓글 생성
    public CommonResponseDto<Object> commentCreate(Long posterId, Long communityId, CommentRequestDto commentRequestDto) {

        //TODO: userID & communityID & iswithDraw(false)
        CommunityUser communityUser = communityUserRepository.findByCommunityId(communityId)
                                                             .orElseThrow(()-> new NotFoundException(ErrorCode.COMMUNITY_USER_NOT_FOUND));

        Poster poster = posterRepository.findById(posterId)
                                        .orElseThrow(() -> new NotFoundException(ErrorCode.POSTER_NOT_FOUND));

        Comment comment = commentRequestDto.toEntity(communityUser,poster);
        commentRepository.save(comment);

        return commonService.successResponse(SuccessCode.COMMENT_CREATE_SUCCESS.getDescription(),HttpStatus.CREATED,null);
    }

    // 댓글 조회
    public CommonResponseDto<Object> allCommentInquiry(Long posterId, Long communityId, int page, int size, String sortBy) {


        Optional<Boolean> commentExists = commentRepository.existsByPosterId(posterId);

        if (!commentExists.orElse(false)) {
            throw new NotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }

        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(sortBy)
                                                                  .descending());

        Page<CommentResponseDto> data = commentRepository.findAllCommentList(posterId, pageable);
        List<CommentResponseDto> commentResponsList = data.getContent();

        PaginationDto paginationDto = PaginationDto.builder()
                                                   .totalPages(data.getTotalPages())
                                                   .totalElements(data.getTotalElements())
                                                   .pageNo(data.getNumber())
                                                   .isLastPage(data.isLast())
                                                   .build();

        CommentListResponseDto commentListResponseDto = CommentListResponseDto.builder()
                                                                              .commentList(commentResponsList)
                                                                              .paginationDto(paginationDto)
                                                                              .build();

        return commonService.successResponse(SuccessCode.ALL_POSTER_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, commentListResponseDto);

    }

    @Transactional
    // 댓글 수정
    public CommonResponseDto<Object> commentUpdate(Long commentId, CommentUpdateDto commentUpdateDto) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        String content = commentUpdateDto.getCommentContent();
        commentRepository.updateCommentContent(commentId, content);

        return commonService.successResponse(SuccessCode.COMMENT_UPDATE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }
}
