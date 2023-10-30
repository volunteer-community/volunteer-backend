package com.maple.volunteer.controller.comment;

import com.maple.volunteer.dto.comment.CommentListResponseDto;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.poster.PosterListResponseDto;
import com.maple.volunteer.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maple")
public class CommentController {

    private final CommentService commentService;

//    @GetMapping("/comment/poster/{posterId}")
//    public ResponseEntity<ResultDto<CommentListResponseDto>> allCommentInquiry(@PathVariable Long posterId) {
//        CommonResponseDto<Object> allCommentInquiry = commentService.allCommentInquiry(posterId);
//        ResultDto<CommentListResponseDto> result = ResultDto.in(allCommentInquiry.getStatus(), allCommentInquiry.getMessage());
//        result.setData((CommentListResponseDto) allCommentInquiry.getData());
//
//        return ResponseEntity.status(allCommentInquiry.getHttpStatus()).body(result);
//    }

}
