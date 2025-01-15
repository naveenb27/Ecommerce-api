package com.backend.backend.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.Model.Product;
import com.backend.backend.Model.ProductIndex;
import com.backend.backend.Repository.ProductIndexRepository;
import com.backend.backend.Repository.ProductRepository;


@Service
public class ProductIndexService{

    @Autowired
    private ProductIndexRepository productIndexRepo;

    @Autowired
    private ProductRepository productRepo;

    public void transferProductsToElasticSearch(){
        List<Product> products = productRepo.findAll();
        
        for(Product product: products) {
            ProductIndex productIndex = new ProductIndex();

            productIndex.setId(String.valueOf(product.getId()));
            productIndex.setName(product.getName());
            productIndex.setMrp(product.getMrp());
            productIndex.setDiscount(product.getDiscount());
            productIndex.setUnits(product.getUnits());
            productIndex.setDescription(product.getDescription());
            productIndex.setLabel(product.getLabel());
            productIndex.setCategory(product.getCategory().getCategoryName());
            productIndexRepo.save(productIndex);
        }
    }

    public Iterable<ProductIndex> getAllProducts(){
        return productIndexRepo.findAll();
    }

    public List<ProductIndex> searchByName(String name){
        return productIndexRepo.findByNameContaining(name);
    }

    public List<ProductIndex> searchByCategory(String category){
        return productIndexRepo.findByCategory(category);
    }

    public List<ProductIndex> searchByDescription(String description){
        return productIndexRepo.findByDescriptionContaining(description);
    }

    public List<ProductIndex> search(String search) {

        search = search.toLowerCase();

        double under = -1;
        double above = -1;

        if (search.contains("above")) {
            String subStrings[] = search.split("above");
            try {
                above = Double.parseDouble(subStrings[1].trim());
            } catch (Exception e) {
                System.out.println("Not a number after 'above'");
            }
            search = subStrings[0].trim(); 
        }
        if (search.contains("under")) {
            String subStrings[] = search.split("under");
            try {
                under = Double.parseDouble(subStrings[1].trim());
            } catch (Exception e) {
                System.out.println("Not a number after 'under'");
            }
            search = subStrings[0].trim();
        }

        String subString[] = search.trim().split("\\s+");

        Set<ProductIndex> uniqueResults = new LinkedHashSet<>();

        for (int i = 0; i < subString.length; i++) {
            List<ProductIndex> name = searchByName(subString[i]);
            List<ProductIndex> category = searchByCategory(subString[i]);
            List<ProductIndex> description = searchByDescription(subString[i]);
            List<ProductIndex> nameFuzz = productIndexRepo.findByNameFuzzy(subString[i]);
            List<ProductIndex> categoryFuzz = productIndexRepo.findByCategoryFuzzy(subString[i]);
            List<ProductIndex> descriptionFuzz = productIndexRepo.findByDescriptionContaining(subString[i]);

            uniqueResults.addAll(name);
            uniqueResults.addAll(category);
            uniqueResults.addAll(description);
            uniqueResults.addAll(nameFuzz);
            uniqueResults.addAll(categoryFuzz);
            uniqueResults.addAll(descriptionFuzz);
        }

        List<ProductIndex> combinedResults = new ArrayList<>(uniqueResults);

        return applyPriceFilters(combinedResults, under, above);
    }

    private List<ProductIndex> applyPriceFilters(List<ProductIndex> products, double under, double above) {
        if (under > 0) {
            products = products.stream()
                    .filter(product -> product.getMrp() < under)
                    .collect(Collectors.toList());
        }

        if (above > 0) {
            products = products.stream()
                    .filter(product -> product.getMrp() > above)
                    .collect(Collectors.toList());
        }

        return products;
    }

}

