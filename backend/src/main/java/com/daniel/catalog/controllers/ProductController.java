package com.daniel.catalog.controllers;

import com.daniel.catalog.dto.ProductDTO;
import com.daniel.catalog.services.Exceptions.ResourceNotFoundException;
import com.daniel.catalog.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/products")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {

        return ResponseEntity.ok().body(productService.findAll(pageable));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findAll(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.findById(id));

    }

    @PostMapping("")
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO ProductDTO) {

        ProductDTO = productService.insert(ProductDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ProductDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(ProductDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO ProductDTO) {

        try {

            ProductDTO = productService.update(id, ProductDTO);

            return ResponseEntity.ok().body(ProductDTO);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found! " + id);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        productService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
