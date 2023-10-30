package com.maple.volunteer.service.comment;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.comment.CommentRepository;
import com.maple.volunteer.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
//    public CommonResponseDto<Object> allCommentInquiry(Long posterId) {
//        Optional<Boolean> posterExists = commentRepository.existsByPosterId(posterId);
//
//        if (!posterExists.orElse(false)) {
//            throw new NotFoundException(ErrorCode.POSTER_NOT_FOUND);
//        }
//    }
}
