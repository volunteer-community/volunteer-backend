package com.maple.volunteer.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {

    private String categoryType; // 카테고리 이름

    @Builder
    public CategoryResponseDto(String categoryType) {
        this.categoryType = categoryType;
    }
}
