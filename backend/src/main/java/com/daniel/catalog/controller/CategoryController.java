package com.daniel.catalog.controller;

import com.daniel.catalog.dto.CategoryDTO;
import com.daniel.catalog.services.CategoryService;
import com.daniel.catalog.services.Exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/categories")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable) {

        return ResponseEntity.ok().body(categoryService.findAll(pageable));

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findAll(@PathVariable Long id) {

        return ResponseEntity.ok().body(categoryService.findById(id));

    }

    @PostMapping("/")
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDTO) {

        categoryDTO = categoryService.insert(categoryDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoryDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(categoryDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {

        try {

            categoryDTO = categoryService.update(id, categoryDTO);

            return ResponseEntity.ok().body(categoryDTO);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found! " + id);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id) {

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
