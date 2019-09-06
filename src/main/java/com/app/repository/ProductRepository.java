package com.app.repository;

import com.app.dto.ProductDto;
import com.app.model.CustomerOrder;
import com.app.model.Product;
import com.app.model.Shop;
import com.app.model.enums.EGuarantee;
import com.app.repository.generic.GenericRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends GenericRepository<Product> {
    Optional<Product> findByName(String productName);

    boolean isExistByNameAndCategoryAndProducer(ProductDto productDTO);

    List<Product> findAllProductsFromSpecificCountryBetweenCustomerAges(String countryName, int ageFrom, int ageTo);

}
