package com.backend.backend.Repository;

import com.backend.backend.Model.Category;
import com.backend.backend.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    List<Product> getByCategory(@Param("id") Long id);

    @Query(value = "SELECT * FROM products ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Product> findProductByPage(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT * FROM products WHERE category_id = :categoryId ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Product> findByCategoryWithPagination(@Param("categoryId") Long categoryId, @Param("limit") int limit, @Param("offset") int offset);
}
