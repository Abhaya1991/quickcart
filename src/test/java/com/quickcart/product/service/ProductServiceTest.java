package com.quickcart.product.service;


import com.quickcart.product.dto.ProductRequest;
import com.quickcart.product.dto.ProductResponse;
import com.quickcart.product.entity.Product;
import com.quickcart.product.mapper.ProductMapper;
import com.quickcart.product.repository.ProductRepository;
import com.quickcart.product.ProductService;
import com.quickcart.users.exception.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product createProduct(Long id, boolean active) {
        return Product.builder()
                .id(id)
                .name("Wireless Mouse")
                .description("Bluetooth wireless mouse")
                .category("Electronics")
                .price(new BigDecimal("799.00"))
                .active(active)
                .build();
    }

    private ProductResponse createResponse(Long id) {
        return new ProductResponse(
                id,
                "Wireless Mouse",
                "Bluetooth wireless mouse",
                "Electronics",
                new BigDecimal("799.00"),
                true,
                null,
                null
        );
    }

    private ProductRequest createRequest() {
        return new ProductRequest(
                "Wireless Mouse",
                "Bluetooth wireless mouse",
                "Electronics",
                new BigDecimal("799.00")
        );
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        ProductRequest request = createRequest();
        Product product = createProduct(1L, true);
        ProductResponse expected = createResponse(1L);

        when(productMapper.toEntity(request)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(expected);

        ProductResponse actual = productService.createProduct(request);

        assertEquals(1L, actual.id());
        assertEquals("Wireless Mouse", actual.name());
        verify(productRepository).save(product);
    }

    @Test
    void getAllActiveProducts_shouldReturnProducts() {
        Product product = createProduct(1L, true);
        ProductResponse response = createResponse(1L);

        when(productRepository.findByActiveTrue())
                .thenReturn(List.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        List<ProductResponse> actual =
                productService.getAllActiveProducts();

        assertEquals(1, actual.size());
        assertEquals("Wireless Mouse", actual.get(0).name());
    }

    @Test
    void getProductById_shouldReturnProduct_whenActive() {
        Product product = createProduct(1L, true);
        ProductResponse response = createResponse(1L);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        ProductResponse actual = productService.getProductById(1L);

        assertEquals(1L, actual.id());
        assertEquals("Wireless Mouse", actual.name());
    }

    @Test
    void getProductById_shouldThrowException_whenMissing() {
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(99L)
        );
    }

    @Test
    void getProductById_shouldThrowException_whenInactive() {
        Product inactiveProduct = createProduct(1L, false);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(inactiveProduct));

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(1L)
        );
    }

    @Test
    void updateProduct_shouldUpdateExistingProduct() {
        Product product = createProduct(1L, true);
        ProductRequest request = new ProductRequest(
                "Gaming Mouse",
                "RGB gaming mouse",
                "Electronics",
                new BigDecimal("1499.00")
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product))
                .thenReturn(new ProductResponse(
                        1L,
                        "Gaming Mouse",
                        "RGB gaming mouse",
                        "Electronics",
                        new BigDecimal("1499.00"),
                        true,
                        null,
                        null
                ));

        ProductResponse actual = productService.updateProduct(1L, request);

        assertEquals("Gaming Mouse", actual.name());
        assertEquals(new BigDecimal("1499.00"), actual.price());
        verify(productRepository).save(product);
    }

    @Test
    void deleteProduct_shouldMarkProductInactive() {
        Product product = createProduct(1L, true);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        productService.deleteProduct(1L);

        assertFalse(product.isActive());
        verify(productRepository).save(product);
    }

    @Test
    void getProducts_shouldReturnPaginatedResults() {
        Product product = createProduct(1L, true);
        ProductResponse response = createResponse(1L);

        Page<Product> productPage = new PageImpl<>(
                List.of(product),
                PageRequest.of(0, 10),
                1
        );

        when(productRepository.findByActiveTrue(any(
                org.springframework.data.domain.Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(response);

        Page<ProductResponse> actual =
                productService.getProducts(0, 10, "id", "asc");

        assertEquals(1, actual.getTotalElements());
        assertEquals("Wireless Mouse", actual.getContent().get(0).name());
    }
}

