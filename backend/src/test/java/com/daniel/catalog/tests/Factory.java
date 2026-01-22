package com.daniel.catalog.tests;

import com.daniel.catalog.dto.ProductDTO;
import com.daniel.catalog.entities.CategoryEntity;
import com.daniel.catalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));

        product.getCategories().add(new CategoryEntity(2L, "Eletronics"));

        return product;

    }

    public static ProductDTO createProductDTO(){

        Product product = createProduct();

        return new ProductDTO(product, product.getCategories());

    }

    public static CategoryEntity createCategory(){

        return new CategoryEntity(1L, "Eletronics");


    }

}
