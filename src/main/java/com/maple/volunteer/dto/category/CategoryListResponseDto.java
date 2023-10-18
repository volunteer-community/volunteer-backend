package com.maple.volunteer.dto.category;


import com.maple.volunteer.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryListResponseDto {

    private List<Category> categoryList;

    @Builder
    public CategoryListResponseDto(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
