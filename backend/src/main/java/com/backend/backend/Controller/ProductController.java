package com.backend.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.backend.backend.Model.Product;
import com.backend.backend.Service.ProductService;
import com.backend.backend.Model.Category;
import com.backend.backend.Service.CategoryService;


import java.util.*;


@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "http://localhost:5173")
class ProductController{

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public List<Product> getProduct(){
        return productService.getAllProducts();
    }

    @GetMapping("/{categoryId}")
    public List<Product> getProductByCategory(@PathVariable Long categoryId){
        return productService.findProductsByCategoryId(categoryId);
    }

    @GetMapping("/{categoryId}/{page}")
    public List<Product> getProductByCategory(@PathVariable Long categoryId, @PathVariable int page){
        return productService.findByCategoryWithPagination(categoryId, page);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product){
        Long category_id = product.getCategory().getCategoryID();
        Category category = categoryService.getCategoryByID(category_id);

        product.setCategory(category);

        return productService.saveProduct(product);
    }

    @GetMapping("/page")
    public List<Product> getProductByPage(@RequestParam(defaultValue = "0") int page){
        return productService.getProductByPage(page);
    }

}