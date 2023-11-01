package com.maple.volunteer.repository.category;

import com.maple.volunteer.domain.category.Category;
import com.maple.volunteer.dto.category.CategoryResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    @Query("SELECT c " +
            "FROM Category c " +
            "WHERE c.id = :categoryId ")
    Optional<Category> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT c " +
            "FROM Category c " +
            "WHERE c.type = :categoryType ")
    Optional<Category> findByCategoryType(@Param("categoryType") String categoryType);

    @Query("SELECT NEW com.maple.volunteer.dto.category.CategoryResponseDto(" +
            "c.id AS categoryId, " +
            "c.type AS categoryType ) "+
            "FROM Category c ")
    List<CategoryResponseDto> findAllCategoryList();
    void deleteById(Long categoryId);

}
