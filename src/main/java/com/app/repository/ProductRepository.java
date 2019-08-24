package com.app.repository;

import com.app.dto.ProductDto;
import com.app.model.Product;
import com.app.model.Shop;
import com.app.repository.generic.GenericRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends GenericRepository<Product> {
    Optional<Product> findByName(String productName);

    boolean isExistByNameAndCategoryAndProducer(ProductDto productDTO);


}
