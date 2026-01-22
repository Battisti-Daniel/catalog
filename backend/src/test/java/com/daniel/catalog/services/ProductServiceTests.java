package com.daniel.catalog.services;

import com.daniel.catalog.dto.ProductDTO;
import com.daniel.catalog.entities.CategoryEntity;
import com.daniel.catalog.entities.Product;
import com.daniel.catalog.repositories.CategoryRepository;
import com.daniel.catalog.repositories.ProductRepository;
import com.daniel.catalog.services.Exceptions.DatabaseException;
import com.daniel.catalog.services.Exceptions.ResourceNotFoundException;
import com.daniel.catalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductServiceTests {

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private CategoryEntity categoryEntity;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 4L;
        product = Factory.createProduct();
        categoryEntity = Factory.createCategory();

        page = new PageImpl<>(List.of(product));

        doNothing().when(repository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        when(repository.existsById(existingId)).thenReturn(true);
        when(repository.existsById(nonExistingId)).thenReturn(false);
        when(repository.existsById(dependentId)).thenReturn(true);
        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        when(repository.findById(existingId)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(repository.getReferenceById(existingId)).thenReturn(product);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(categoryRepository.getReferenceById(existingId)).thenReturn(categoryEntity);
        when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        verify(repository, times(1)).deleteById(existingId);

    }

    @Test
    public void deleteShouldTheowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });

    }

    @Test
    public void findAllPagedShouldReturnPage(){

        Pageable pageable = PageRequest.of(0,10);

        Page<ProductDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);

    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){

        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });

    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists(){

        ProductDTO productReplaced = Factory.createProductDTO();

        productReplaced = service.update(existingId, productReplaced);

        Assertions.assertNotNull(productReplaced);

    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdNotExists(){

        ProductDTO productReplaced = Factory.createProductDTO();

        Assertions.assertThrows(EntityNotFoundException.class, () -> {

            service.update(nonExistingId, productReplaced);

        });

    }

}
