package com.app.service.services;

import com.app.dto.CategoryDto;
import com.app.dto.ProducerDto;
import com.app.dto.ProductDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Category;
import com.app.model.Producer;
import com.app.model.Product;
import com.app.model.enums.EGuarantee;
import com.app.repository.CategoryRepository;
import com.app.repository.CountryRepository;
import com.app.repository.ProducerRepository;
import com.app.repository.ProductRepository;
import com.app.repository.impl.CategoryRepositoryImpl;
import com.app.repository.impl.CountryRepositoryImpl;
import com.app.repository.impl.ProducerRepositoryImpl;
import com.app.repository.impl.ProductRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CategoryValidator;
import com.app.validation.impl.CountryValidator;
import com.app.validation.impl.ProductValidator;

import java.util.*;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    private final ProducerRepository producerRepository = new ProducerRepositoryImpl();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();

    private final ProductValidator productValidator = new ProductValidator();
    private final CategoryValidator categoryValidator = new CategoryValidator();
    private final CountryValidator countryValidator = new CountryValidator();

    public ProductDto addProduct(ProductDto productDto) {

        productValidator.validateProduct(productDto);

        final boolean exist = productRepository.isExistByNameAndCategoryAndProducer(productDto);

        if (exist) {
            throw new MyException(ExceptionCode.PRODUCT, "PRODUCT WITH GIVEN CATEGORY AND PRODUCER EXIST");
        }
        Product product = Mappers.fromProductDtoToProduct(productDto);

        CategoryDto categoryDto = productDto.getCategoryDto();
        if (categoryDto == null) {
            throw new MyException(ExceptionCode.CATEGORY, "CATEGORY IS NULL");
        }
        Category category = categoryRepository
                .findByName(categoryDto.getName())
                .orElse(null);

        if (category == null) {
            categoryValidator.validateCategory(productDto.getCategoryDto());
            category = Mappers.fromCategoryDtoToCategory(categoryDto);
            category = categoryRepository.addOrUpdate(category).orElseThrow(() -> new MyException(ExceptionCode.CATEGORY, "CAN NOT ADD CATEGORY IN PRODUCT SERVICE"));
        }

        ProducerDto producerDto = productDto.getProducerDto();
        if (producerDto == null) {
            throw new MyException(ExceptionCode.PRODUCER, "PRODUCER IS NULL");
        }

        Producer producer = producerRepository
                .findByNameAndCountry(producerDto)
                .orElseThrow(() -> new MyException(ExceptionCode.PRODUCER, "PRODUCER WAS NOT FOUND. PLEASE ADD PRODUCER FIRST"));

        product.setProducer(producer);
        product.setCategory(category);
        productRepository.addOrUpdate(product);
        return Mappers.fromProductToProductDto(product);
    }

    public LinkedHashMap<Category, Optional<ProductDto>> findProductsWithBiggestPriceInCategory() {
        return productRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.toList()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        products -> products
                                .getValue()
                                .stream()
                                .map(Mappers::fromProductToProductDto)
                                .max(Comparator.comparing(ProductDto::getPrice)),
                        (k, v) -> k,
                        LinkedHashMap::new
                ));
    }

    public List<ProductDto> findAllProductsFromSpecificCountryBetweenCustomerAges(String country, int ageFrom, int ageTo) {
        if (country == null) {
            throw new MyException(ExceptionCode.PRODUCT, "COUNTRY CAN NOT BE NULL");
        }
        if (ageFrom < 18) {
            throw new MyException(ExceptionCode.AGE_UNDER_18, "LOWER AGE LIMIT CAN NOT BE LESS THAN 18");
        }
        if (ageFrom > ageTo) {
            throw new MyException(ExceptionCode.INCORRECT_AGE, "LOWER AGE LIMIT CAN NOT BE HIGHER THAN UPPER LIMIT OF AGE");
        }

        return productRepository.findAllProductsFromSpecificCountryBetweenCustomerAges(country, ageFrom, ageTo)
                .stream()
                .map(Mappers::fromProductToProductDto)
                .collect(Collectors.toList());

    }

    public List<ProductDto> findAllProductsWithGivenGuarantees(Set<EGuarantee> eGuarantees) {
        if (eGuarantees == null) {
            throw new MyException(ExceptionCode.EGUARANTEES, "GUARANTEES CAN NOT BE NULL");
        }

        return productRepository.findAll()
                .stream()
                .filter(prGuarantees -> prGuarantees.getEGuarantees().containsAll(eGuarantees))
                .map(Mappers::fromProductToProductDto)
                .collect(Collectors.toList());
    }
}
