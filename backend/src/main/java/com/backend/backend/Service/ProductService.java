package com.backend.backend.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.Model.Product;
import com.backend.backend.Repository.ProductRepository;

@Service
public class ProductService{

    @Autowired
    private ProductRepository productRepo;

    public Product saveProduct(Product product){
        return productRepo.save(product);
    }    

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public List<Product> findProductsByCategoryId(Long id){
        return productRepo.getByCategory(id);
    }

    public List<Product> getProductByPage(int pages){
        int offset = (pages-1) * 6;
        if(offset < 0) offset = 0;

        return productRepo.findProductByPage(6 , offset);
    }

    public List<Product> findByCategoryWithPagination(Long id, int pages){
        int offset = (pages-1) * 6;
        if(offset < 0) offset = 0;

        return productRepo.findByCategoryWithPagination(id, 6, offset);
    }

    public List<Product> filterProducts(int page, double minMrp, double maxMrp, double minDiscount, double maxDiscount, List<String> label){
        int offset = (page - 1) * 6;
        if(offset < 0) offset = 0;

        return productRepo.filterProductsNative(offset, 6, minMrp, maxMrp, minDiscount, maxDiscount, label);
    }

    public List<Product> addAllProducts(List<Product> products){
        return productRepo.saveAll(products);
    }
}
