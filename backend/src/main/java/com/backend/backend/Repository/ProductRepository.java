package com.backend.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.backend.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    List<Product> getByCategory(@Param("id") Long id);

    @Query(value = "SELECT * FROM products ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Product> findProductByPage(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT * FROM products WHERE category_id = :categoryId ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Product> findByCategoryWithPagination(@Param("categoryId") Long categoryId, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT * FROM products WHERE " +
                "(:minMrp IS NULL OR mrp >= :minMrp) AND " +
                "(:maxMrp IS NULL OR mrp <= :maxMrp) AND " +
                "(:minDiscount IS NULL OR discount >= :minDiscount) AND " +
                "(:maxDiscount IS NULL OR discount <= :maxDiscount) AND " +
                "(:labels IS NULL OR label IN (:labels)) " +
                "ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Product> filterProductsNative(
        @Param("offset") int offset,
        @Param("limit") int limit,
        @Param("minMrp") Double minMrp,
        @Param("maxMrp") Double maxMrp,
        @Param("minDiscount") Double minDiscount,
        @Param("maxDiscount") Double maxDiscount,
        @Param("labels") List<String> labels
    );

}




