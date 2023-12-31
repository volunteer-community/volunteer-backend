package com.maple.volunteer.controller.category;

import com.maple.volunteer.dto.category.CategoryListResponseDto;
import com.maple.volunteer.dto.category.CategoryRequestDto;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maple")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 저장 API
    @PostMapping("/category")
    public ResponseEntity<ResultDto<Void>> categoryCreate(@RequestBody CategoryRequestDto categoryRequestDto) {
        CommonResponseDto<Object> categoryCreate = categoryService.categoryCreate(categoryRequestDto);
        ResultDto<Void> result = ResultDto.in(categoryCreate.getStatus(), categoryCreate.getMessage());

        return ResponseEntity.status(categoryCreate.getHttpStatus()).body(result);
    }

    // 카테고리 조회 API (리스트 반환)
    @GetMapping("/category")
    public ResponseEntity<ResultDto<CategoryListResponseDto>> categoryInquiry() {
        CommonResponseDto<Object> categoryInquiry = categoryService.categoryInquiry();
        ResultDto<CategoryListResponseDto> result = ResultDto.in(categoryInquiry.getStatus(), categoryInquiry.getMessage());
        result.setData((CategoryListResponseDto) categoryInquiry.getData());

        return ResponseEntity.status(categoryInquiry.getHttpStatus()).body(result);
    }

    // 카테고리 변경 API
    @PatchMapping("/category/{categoryId}")
    public ResponseEntity<ResultDto<Void>> categoryUpdate(@PathVariable(value = "categoryId") Long categoryId,
                                                          @RequestBody CategoryRequestDto categoryRequestDto) {
        CommonResponseDto<Object> categoryUpdate = categoryService.categoryUpdate(categoryId, categoryRequestDto);
        ResultDto<Void> result = ResultDto.in(categoryUpdate.getStatus(), categoryUpdate.getMessage());

        return ResponseEntity.status(categoryUpdate.getHttpStatus()).body(result);
    }

    // 카테고리 삭제 API
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ResultDto<Void>> categoryDelete(@PathVariable(value = "categoryId") Long categoryId) {
        CommonResponseDto<Object> categoryDelete = categoryService.categoryDelete(categoryId);
        ResultDto<Void> result = ResultDto.in(categoryDelete.getStatus(), categoryDelete.getMessage());

        return ResponseEntity.status(categoryDelete.getHttpStatus()).body(result);
    }
}
