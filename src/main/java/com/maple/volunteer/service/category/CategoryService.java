package com.maple.volunteer.service.category;

import com.maple.volunteer.domain.category.Category;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.category.CategoryListResponseDto;
import com.maple.volunteer.dto.category.CategoryRequestDto;
import com.maple.volunteer.dto.category.CategoryResponseDto;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.category.CategoryRepository;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.Role;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;

    // 카테고리 저장
    @Transactional
    public CommonResponseDto<Object> categoryCreate(CategoryRequestDto categoryRequestDto) {

        // 커뮤니티 저장
        categoryRepository.save(categoryRequestDto.toEntity());

        return commonService.successResponse(SuccessCode.CATEGORY_INSERT_SUCCESS.getDescription(), HttpStatus.CREATED, null);
    }

    // 카테고리 조회
    public CommonResponseDto<Object> categoryInquiry() {

        // 커뮤니티 리스트 가져오기
        List<CategoryResponseDto> categoryList = categoryRepository.findAllCategoryList();

        CategoryListResponseDto categoryListResponseDto = CategoryListResponseDto.builder()
                .categoryList(categoryList)
                .build();

        return commonService.successResponse(SuccessCode.CATEGORY_INQUIRY_SUCCESS.getDescription(), HttpStatus.OK, categoryListResponseDto);
    }

    // 카테고리 수정
    @Transactional
    public CommonResponseDto<Object> categoryUpdate(Long categoryId, CategoryRequestDto categoryRequestDto) {

        // 카테고리 가져오기
        Category category = categoryRepository.findByCategoryId(categoryId)
                // 값이 없다면 오류 반환
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_ID_NOT_FOUND));

        // 카테고리 업데이트
        category.categoryUpdate(categoryRequestDto.getCategoryType());

        return commonService.successResponse(SuccessCode.CATEGORY_UPDATE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 카테고리 삭제
    @Transactional
    public CommonResponseDto<Object> categoryDelete(Long categoryId) {

        // 카테고리 삭제
        categoryRepository.deleteById(categoryId);

        return commonService.successResponse(SuccessCode.CATEGORY_DELETE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }
}
