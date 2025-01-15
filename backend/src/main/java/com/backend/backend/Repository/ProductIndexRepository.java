package com.backend.backend.Repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.backend.backend.Model.ProductIndex;

public interface ProductIndexRepository extends ElasticsearchRepository<ProductIndex, String> {
    List<ProductIndex> findByNameContaining(String name);
    List<ProductIndex> findByCategory(String category);
    List<ProductIndex> findByDescriptionContaining(String description);

    @Query("{\"fuzzy\": {\"name\": {\"value\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<ProductIndex> findByNameFuzzy(String name);

    @Query("{\"fuzzy\": {\"category\": {\"value\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<ProductIndex> findByCategoryFuzzy(String category);

    @Query("{\"fuzzy\": {\"description\": {\"value\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<ProductIndex> findByDescriptionFuzzy(String description);
}
