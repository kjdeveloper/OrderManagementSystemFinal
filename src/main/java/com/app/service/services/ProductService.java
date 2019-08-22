package com.app.service.services;

import com.app.dto.CategoryDto;
import com.app.dto.ProducerDto;
import com.app.dto.ProductDto;
import com.app.dto.TradeDto;
import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.model.enums.EGuarantee;
import com.app.repository.*;
import com.app.repository.impl.*;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    private final ProducerRepository producerRepository = new ProducerRepositoryImpl();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();
    private final TradeRepository tradeRepository = new TradeRepositoryImpl();
    private CustomerOrderRepository customerOrderRepository = new CustomerOrderRepositoryImpl();

    private final ProductValidator productValidator = new ProductValidator();
    private final CategoryValidator categoryValidator = new CategoryValidator();
    private final ProducerValidator producerValidator = new ProducerValidator();
    private final CountryValidator countryValidator = new CountryValidator();
    private final TradeValidator tradeValidator = new TradeValidator();

    private final UserDataService userDataService = new UserDataService();

    public ProductDto addProduct(ProductDto productDto){
        if (productDto == null){
            throw new MyException("PRODUCTDTO IS NULL");
        }

        productValidator.validateProduct(productDto);
        categoryValidator.validateCategory(productDto.getCategoryDto());
        producerValidator.validateProducer(productDto.getProducerDto());
        countryValidator.validateCountry(productDto.getProducerDto().getCountryDto());

        final boolean exist = productRepository.isExistByNameAndCategoryAndProducer(productDto);

        if (exist) {
            throw new MyException("PRODUCT WITH GIVEN CATEGORY AND PRODUCER EXIST");
        }
        Product product = Mappers.fromProductDtoToProduct(productDto);

        CategoryDto categoryDto = productDto.getCategoryDto();
        if (categoryDto == null){
            throw new MyException("CATEGORY IS NULL");
        }
        Category category = categoryRepository
                .findByName(categoryDto)
                .orElseThrow(() -> new MyException("CATEGORY WAS NOT FOUND"));

        ProducerDto producerDto = productDto.getProducerDto();
        if (producerDto == null){
            throw new MyException("PRODUCER IS NULL");
        }
        Producer producer = producerRepository
                .findByName(producerDto)
                .orElseThrow(() -> new MyException("PRODUCER WAS NOT FOUND;"));

        product.setProducer(producer);
        product.setCategory(category);
        productRepository.addOrUpdate(product);
        return Mappers.fromProductToProductDto(product);
    }

    public Map<Category, Product> findBiggestPriceInCategory() {
        return productRepository.findAll()
                .stream()
                .peek(s -> System.out.println(s))
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(Product::getPrice)), proOp -> proOp.orElseThrow(NullPointerException::new))));
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

        return customerOrderRepository.findAll()
                .stream()
                .filter(cs -> cs.getCustomer().getCountry().getName().equals(country))
                .filter(age -> ageFrom < age.getCustomer().getAge() && age.getCustomer().getAge() < ageTo)
                .map(CustomerOrder::getProduct)
                .sorted(Comparator.comparing(Product::getPrice))
                .map(Mappers::fromProductToProductDto)
                .collect(Collectors.toList());
    }

    public Set<ProductDto> findAllProductsWithGivenGuarantees(Set<EGuarantee> eGuarantees) {
        if (eGuarantees == null) {
            throw new MyException("EGUARANTEES CAN NOT BE NULL");
        }

        return productRepository.findAll()
                .stream()
                .filter(pr -> pr.getEGuarantees() == eGuarantees)
                .map(Mappers::fromProductToProductDto)
                .collect(Collectors.toSet());
    }
}
