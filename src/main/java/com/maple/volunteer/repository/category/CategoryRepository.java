package com.maple.volunteer.repository.category;

import com.maple.volunteer.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
