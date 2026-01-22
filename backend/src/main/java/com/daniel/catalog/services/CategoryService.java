package com.daniel.catalog.services;

import com.daniel.catalog.dto.CategoryDTO;
import com.daniel.catalog.entities.CategoryEntity;
import com.daniel.catalog.repositories.CategoryRepository;
import com.daniel.catalog.services.Exceptions.DatabaseException;
import com.daniel.catalog.services.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {

        Page<CategoryEntity> list = categoryRepository.findAll(pageable);

        return list.map(CategoryDTO::new);

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {

        Optional<CategoryEntity> obj = categoryRepository.findById(id);

        CategoryEntity entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return new CategoryDTO(entity);

    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {

        CategoryEntity category = new CategoryEntity();
        category.setName(categoryDTO.getName());

        category = categoryRepository.save(category);

        return new CategoryDTO(category);

    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {

        CategoryEntity entity = categoryRepository.getReferenceById(id);

        entity.setName(categoryDTO.getName());

        categoryRepository.save(entity);

        return new CategoryDTO(entity);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource Not Found!");
        }
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity Violation!");
        }

    }
}
