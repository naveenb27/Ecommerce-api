package com.backend.backend.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.Model.ProductIndex;
import com.backend.backend.Service.ProductIndexService;

@RestController
@RequestMapping("/api/elastic/products")
public class ProductIndexController{

    @Autowired
    private ProductIndexService productIndexService;

    @GetMapping("/fetch-data")
    public String createProduct(){
        try{
            productIndexService.transferProductsToElasticSearch();
            return "Datas are fetched";
        }catch(Exception e){
            System.out.println(e);
            return "Datas aren't fetched";
        }
    }

    @GetMapping("/search")
    public List<ProductIndex> search(@RequestParam String search){
        return productIndexService.search(search);
    }

    @GetMapping("/searchByName")
    public List<ProductIndex> searchByName(@RequestParam String name) {
        return productIndexService.searchByName(name);
    }   

    @GetMapping("/searchByCategory")
    public List<ProductIndex> searchByCategory(@RequestParam String category){
        return productIndexService.searchByCategory(category);
    }

    @GetMapping("/searchByDescription")
    public List<ProductIndex> searchByDescription(@RequestParam String description){
        System.out.println("Description: "+ description);
        return productIndexService.searchByDescription(description);
    }

    @GetMapping
    public Iterable<ProductIndex> getAllProducts() {
        return productIndexService.getAllProducts();
    }

}
