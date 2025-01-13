package com.backend.backend.Model;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "category")
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //When a new entity is persisted, the database assigns the next available value in the sequence to the primary key column.
    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryID;


    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @Temporal(TemporalType.DATE)
    @Column(name = "category_datex")
    private Date createdDate;


    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
 
    public void setCategoryID(Long categoryID){
        this.categoryID = categoryID;
    }

    public void setCreatedDate(Date createdDate){
        this.createdDate = createdDate;
    }

    public long getCategoryID(){
        return categoryID;
    }

    public Date getCategoryDate(){
        return createdDate;
    }

    public String getCategoryName(){
        return categoryName;
    }

}