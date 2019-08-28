package com.app.service.services;

import com.app.dto.CategoryDto;
import com.app.dto.ProducerDto;
import com.app.dto.ProductDto;
import com.app.exceptions.MyException;
import com.app.model.Category;
import com.app.model.CustomerOrder;
import com.app.model.Producer;
import com.app.model.Product;
import com.app.model.enums.EGuarantee;
import com.app.repository.CategoryRepository;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.ProducerRepository;
import com.app.repository.ProductRepository;
import com.app.repository.impl.CategoryRepositoryImpl;
import com.app.repository.impl.CustomerOrderRepositoryImpl;
import com.app.repository.impl.ProducerRepositoryImpl;
import com.app.repository.impl.ProductRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CategoryValidator;
import com.app.validation.impl.ProductValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    private final ProducerRepository producerRepository = new ProducerRepositoryImpl();

    private CustomerOrderRepository customerOrderRepository = new CustomerOrderRepositoryImpl();

    private final ProductValidator productValidator = new ProductValidator();
    private final CategoryValidator categoryValidator = new CategoryValidator();


    public ProductDto addProduct(ProductDto productDto) {

        productValidator.validateProduct(productDto);

        final boolean exist = productRepository.isExistByNameAndCategoryAndProducer(productDto);

        if (exist) {
            throw new MyException("PRODUCT WITH GIVEN CATEGORY AND PRODUCER EXIST");
        }
        Product product = Mappers.fromProductDtoToProduct(productDto);

        CategoryDto categoryDto = productDto.getCategoryDto();
        if (categoryDto == null) {
            throw new MyException("CATEGORY IS NULL");
        }
        Category category = categoryRepository
                .findByName(categoryDto.getName())
                .orElse(null);

        if (category == null) {
            categoryValidator.validateCategory(productDto.getCategoryDto());
            category = Mappers.fromCategoryDtoToCategory(categoryDto);
            category = categoryRepository.addOrUpdate(category).orElseThrow(() -> new MyException("CAN NOT ADD CATEGORY IN PRODUCT SERVICE"));
        }

        ProducerDto producerDto = productDto.getProducerDto();
        if (producerDto == null) {
            throw new MyException("PRODUCER IS NULL");
        }
        Producer producer = producerRepository
                .findByName(producerDto.getName())
                .orElseThrow(() -> new MyException("PRODUCER WAS NOT FOUND. PLEASE ADD PRODUCER THAN PRODUCT"));

        product.setProducer(producer);
        product.setCategory(category);
        productRepository.addOrUpdate(product);
        return Mappers.fromProductToProductDto(product);
    }

    public List<ProductDto> findProductsWithBiggestPriceInCategory() {
        return productRepository.findProductsWithBiggestPriceInCategory()
                .stream()
                .map(Mappers::fromProductToProductDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> findAllProductsFromSpecificCountryBetweenCustomerAges(String country, int ageFrom, int ageTo) {
        if (country == null) {
            throw new MyException("COUNTRY CAN NOT BE NULL");
        }
        if (ageFrom < 18) {
            throw new MyException("LOWER AGE LIMIT CAN NOT BE LESS THAN 18");
        }
        if (ageFrom > ageTo) {
            throw new MyException("LOWER AGE LIMIT CAN NOT BE HIGHER THAN UPPER LIMIT OF AGE");
        }

        return productRepository.findAllProductsFromSpecificCountryBetweenCustomerAges(country, ageFrom, ageTo)
                .stream()
                .map(Mappers::fromProductToProductDto)
                .collect(Collectors.toList());

    }

    public Set<ProductDto> findAllProductsWithGivenGuarantees(Set<EGuarantee> eGuarantees) {
        if (eGuarantees == null) {
            throw new MyException("GUARANTEES CAN NOT BE NULL");
        }

        return productRepository.findAllProductsWithGivenGuarantees(eGuarantees)
                .stream()
                .map(Mappers::fromProductToProductDto)
                .collect(Collectors.toSet());
    }
}
