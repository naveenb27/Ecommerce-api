package com.backend.backend.Repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.backend.backend.Model.ProductIndex;


public interface ProductIndexRepository extends ElasticsearchRepository<ProductIndex, String> {
    List<ProductIndex> findByName(String name);
    List<ProductIndex> findByNameContaining(String name);
    List<ProductIndex> findByCategory(String category);
    List<ProductIndex> findByDescriptionContaining(String Description);
    // List<ProductIndex> findByCategoryName(String categoryName);
}