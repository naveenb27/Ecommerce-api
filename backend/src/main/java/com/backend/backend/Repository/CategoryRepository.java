package com.backend.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.backend.Model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM category ORDER BY category_name LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Category> findCategoryByPage(@Param("limit") int limit, @Param("offset") int offset);
}
