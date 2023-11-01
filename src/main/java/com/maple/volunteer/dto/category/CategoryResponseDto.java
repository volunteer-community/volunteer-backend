package com.maple.volunteer.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {

    private Long categoryId; // 카테고리 ID
    private String categoryType; // 카테고리 이름

    @Builder
    public CategoryResponseDto(Long categoryId, String categoryType) {
        this.categoryId = categoryId;
        this.categoryType = categoryType;
    }
}
