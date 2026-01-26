package com.daniel.catalog.controllers;

import com.daniel.catalog.dto.UserDTO;
import com.daniel.catalog.dto.UserInsertDTO;
import com.daniel.catalog.services.Exceptions.ResourceNotFoundException;
import com.daniel.catalog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserService productService;

    @GetMapping("")
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {

        return ResponseEntity.ok().body(productService.findAll(pageable));

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findAll(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.findById(id));

    }

    @PostMapping("")
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO UserDTO) {

        UserDTO newDto = productService.insert(UserDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();

        return ResponseEntity.created(uri).body(newDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO UserDTO) {

        try {

            UserDTO = productService.update(id, UserDTO);

            return ResponseEntity.ok().body(UserDTO);

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
