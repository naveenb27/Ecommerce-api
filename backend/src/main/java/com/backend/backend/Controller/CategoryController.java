package com.backend.backend.Controller;

import com.backend.backend.Model.Category;
import com.backend.backend.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/category")
public class CategoryController{

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/page")
    public List<Category> getCategoriesByPage(@RequestParam(defaultValue = "0") int page){
            System.out.println(page);
            return categoryService.getCategoryByPage(page);

    }

    @PostMapping
    public Category createdCategory(@RequestBody Category category){
        System.out.println(category);
        return categoryService.saveCategory(category);
    }
}