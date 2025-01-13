package com.backend.backend.Model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document(indexName="producindex")
public class ProductIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Double)
    private double mrp;

    @Field(type = FieldType.Double)
    private double discount;

    @Field(type = FieldType.Long)
    private long units;

    @Field(type = FieldType.Text)
    private String label;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String category; 
}
