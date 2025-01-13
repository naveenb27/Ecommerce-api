package com.backend.backend.Controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.Model.Category;
import com.backend.backend.Model.Product;
import com.backend.backend.Service.CategoryService;
import com.backend.backend.Service.ProductService;


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


    @GetMapping("/filter/values")
    public List<Product> filterProduct(@RequestParam(defaultValue = "0") int page, 
                                    @RequestParam(required = false, defaultValue="0.0") Double minMrp,
                                    @RequestParam(required = false, defaultValue="1.7976931348623157E308") Double maxMrp, 
                                    @RequestParam(required = false, defaultValue="0.0") Double minDiscount, 
                                    @RequestParam(required = false, defaultValue="100.0") Double maxDiscount, 
                                    @RequestParam(required = false) String label ){
        List<String> labels = Arrays.asList(label.split(","));
        System.out.println("Page : " + page + " label : " + labels);
        return productService.filterProducts(page, minMrp, maxMrp, minDiscount, maxDiscount, labels);
    }
}