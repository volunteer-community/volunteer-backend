package com.maple.volunteer.dto.category;

import com.maple.volunteer.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRequestDto {

    private String categoryType;

    @Builder
    public CategoryRequestDto(String categoryType) {
        this.categoryType = categoryType;
    }

    public Category toEntity() {
        return Category.builder()
                .type(categoryType)
                .build();
    }
}
