package com.backend.backend.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend.Model.Category;
import com.backend.backend.Repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (OptimisticLockingFailureException e) {
            System.err.println("Conflict detected while saving category: " + e.getMessage());
            throw e; 
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByID(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Category> getCategoryByPage(int page) {
        int pageSize = 3;
        int offset = Math.max((page - 1) * pageSize, 0);
        return categoryRepository.findCategoryByPage(pageSize, offset);
    }

    public void deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error deleting category with ID " + id + ": " + e.getMessage());
        }
    }

    public List<Category> saveAllCategories(List<Category> categories) {
        try {
            return categoryRepository.saveAll(categories);
        } catch (OptimisticLockingFailureException e) {
            System.err.println("Conflict detected while saving categories: " + e.getMessage());
            throw e; 
        }
    }
}
