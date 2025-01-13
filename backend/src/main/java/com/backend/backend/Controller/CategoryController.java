package com.backend.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.Model.Category;
import com.backend.backend.Service.CategoryService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/page")
    public List<Category> getCategoriesByPage(@RequestParam(defaultValue = "0") int page) {
        System.out.println(page);
        return categoryService.getCategoryByPage(page);
    }

    @PostMapping("/addAllCategories")
    public List<Category> addAllCategories(@RequestBody List<Category> categories) {
        System.out.println(categories);
        return categoryService.saveAllCategories(categories);
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        System.out.println(category);
        return categoryService.saveCategory(category);
    }
}
