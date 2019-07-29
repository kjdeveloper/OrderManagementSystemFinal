package com.app.repository;

import com.app.dto.ProductDto;
import com.app.model.Product;
import com.app.repository.generic.GenericRepository;

import java.util.Optional;

public interface ProductRepository extends GenericRepository<Product> {
    Optional<Product> findByName(String name);

    boolean isExistByNameAndCategoryAndProducer(ProductDto productDTO);
}
