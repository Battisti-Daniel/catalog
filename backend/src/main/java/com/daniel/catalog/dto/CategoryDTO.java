package com.daniel.catalog.dto;

import com.daniel.catalog.entities.CategoryEntity;

public class CategoryDTO {

    private Long id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(CategoryEntity categoryEntity) {
        this.id = categoryEntity.getId();
        this.name = categoryEntity.getName();
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
