package com.maple.volunteer.service.poster;

import com.maple.volunteer.domain.communityuser.CommunityUser;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.dto.poster.*;
import com.maple.volunteer.exception.NotFoundException;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PosterService {

    private final CommonService commonService;
    private final PosterRepository posterRepository;
    private final CommunityUserRepository communityUserRepository;

    //전체조회
    public CommonResponseDto<Object> allPosterInquiry(Long communityId, int page, int size, String sortBy) {

        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(sortBy)
                                                                  .descending());


        Page<PosterResponseDto> data = posterRepository.findAllPosterList(communityId, pageable);
        List<PosterResponseDto> posterResponseList = data.getContent();

        PaginationDto paginationDto = PaginationDto.builder()
                                                   .totalPages(data.getTotalPages())
                                                   .totalElements(data.getTotalElements())
                                                   .pageNo(data.getNumber())
                                                   .isLastPage(data.isLast())
                                                   .build();

        PosterListResponseDto posterListResponseDto = PosterListResponseDto.builder()
                                                                           .posterList(posterResponseList)
                                                                           .paginationDto(paginationDto)
                                                                           .build();

        return commonService.successResponse(SuccessCode.ALL_POSTER_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, posterListResponseDto);
    }

    //상세조회
    public CommonResponseDto<Object> posterDetailInquiry(Long posterId, Long communityId) {

        PosterDetailResponseDto posterDetailResponseDto = posterRepository.findPosterDetailByCommunityIdAndPosterId(communityId, posterId)
                                                                          .orElseThrow(() -> new NotFoundException(ErrorCode.POSTER_NOT_FOUND));

        PosterDetailListResponseDto posterDetailListResponseDto = PosterDetailListResponseDto.builder()
                                                                                             .posterDetail(posterDetailResponseDto)
                                                                                             .build();

        return commonService.successResponse(SuccessCode.ALL_POSTER_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, posterDetailListResponseDto);
    }


}
