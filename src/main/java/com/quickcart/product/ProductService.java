package com.quickcart.product;

import com.quickcart.product.dto.ProductRequest;
import com.quickcart.product.dto.ProductResponse;
import com.quickcart.product.entity.Product;
import com.quickcart.product.mapper.ProductMapper;
import com.quickcart.product.repository.ProductRepository;
import com.quickcart.users.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllActiveProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active product not found with id: " + id));

        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository
                .findByCategoryIgnoreCaseAndActiveTrue(category)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active product not found with id: " + id));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setPrice(request.price());

        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active product not found with id: " + id));

        product.setActive(false);

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(int page, int size, String sortBy,
                                             String direction) {

        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return productRepository.findByActiveTrue(pageable)
                .map(productMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(
            String category,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return productRepository
                .findByCategoryIgnoreCaseAndActiveTrue(category, pageable)
                .map(productMapper::toResponse);
    }
}
