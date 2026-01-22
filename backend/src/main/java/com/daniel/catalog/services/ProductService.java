package com.daniel.catalog.services;

import com.daniel.catalog.dto.CategoryDTO;
import com.daniel.catalog.dto.ProductDTO;
import com.daniel.catalog.entities.CategoryEntity;
import com.daniel.catalog.entities.Product;
import com.daniel.catalog.repositories.CategoryRepository;
import com.daniel.catalog.repositories.ProductRepository;
import com.daniel.catalog.services.Exceptions.DatabaseException;
import com.daniel.catalog.services.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {

        Page<Product> list = productService.findAll(pageable);

        return list.map(ProductDTO::new);

    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {

        Optional<Product> obj = productService.findById(id);

        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return new ProductDTO(entity, entity.getCategories());

    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {

        Product category = new Product();

        copyDtoToEntity(productDTO, category);

        category = productService.save(category);

        return new ProductDTO(category);

    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO categoryDTO) {

        Product entity = productService.getReferenceById(id);

        copyDtoToEntity(categoryDTO, entity);

        productService.save(entity);

        return new ProductDTO(entity);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        if (!productService.existsById(id)) {
            throw new ResourceNotFoundException("Resource Not Found!");
        }
        try {
            productService.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity Violation!");
        }

    }

    private void copyDtoToEntity(ProductDTO categoryDTO, Product category) {

        category.setDate(categoryDTO.getDate());
        category.setPrice(categoryDTO.getPrice());
        category.setDescription(categoryDTO.getDescription());
        category.setName(categoryDTO.getName());
        category.setImgUrl(category.getImgUrl());

        category.getCategories().clear();

        for(CategoryDTO catDto : categoryDTO.getCategories()){
            CategoryEntity entity = categoryRepository.getReferenceById(catDto.getId());

            category.getCategories().add(entity);

        }

    }


}
