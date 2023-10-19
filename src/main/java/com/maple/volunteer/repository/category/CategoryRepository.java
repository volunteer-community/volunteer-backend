package com.maple.volunteer.repository.category;

import com.maple.volunteer.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    void deleteById(Long categoryId);

}
