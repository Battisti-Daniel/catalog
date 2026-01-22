package com.daniel.catalog.repositories;

import com.daniel.catalog.entities.Product;
import com.daniel.catalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long existingId;
    private long countTotalProducts;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp() throws Exception{

        existingId = 1;
        countTotalProducts = 25L;

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());

    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){

        Product product = Factory.createProduct();

        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());

    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists(){

        Optional<Product> product = repository.findById(existingId);

        Assertions.assertTrue(product.isPresent());

    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdNotExists(){

        Optional<Product> product = repository.findById(100L);

        Assertions.assertTrue(product.isEmpty());

    }



}
