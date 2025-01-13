package com.backend.backend.Service;

import com.backend.backend.Model.Category;
import com.backend.backend.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryByID(Long id){
        return categoryRepository.findById(id).orElse(null);
    } 

    public List<Category> getCategoryByPage(int page){
        int offset = (page-1) * 2;
        if (offset < 0) offset = 0; 
        return categoryRepository.findCategoryByPage(2, offset);
    }

    public void deleteCategory(Long id){
        
        try{
            categoryRepository.deleteById(id);
        }catch(Exception err){
            System.out.println("Wrong ID! " +  err);
        }
    }
}