package com.app.service.services;

import com.app.dto.ProducerDto;
import com.app.dto.ProductDto;
import com.app.dto.TradeDto;
import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.repository.*;
import com.app.repository.impl.*;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CategoryValidator;
import com.app.validation.impl.CountryValidator;
import com.app.validation.impl.ProducerValidator;
import com.app.validation.impl.ProductValidator;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    private final ProducerRepository producerRepository = new ProducerRepositoryImpl();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();
    private final TradeRepository tradeRepository = new TradeRepositoryImpl();

    private final ProductValidator productValidator = new ProductValidator();
    private final CategoryValidator categoryValidator = new CategoryValidator();
    private final ProducerValidator producerValidator = new ProducerValidator();
    private final CountryValidator countryValidator = new CountryValidator();

    private final UserDataService userDataService = new UserDataService();

    public ProductDto addProduct(ProductDto productDto) {
        productValidator.validateProduct(productDto);
        categoryValidator.validateCategory(productDto.getCategoryDTO());
        producerValidator.validateProducer(productDto.getProducerDTO());
        countryValidator.validateCountry(productDto.getProducerDTO().getCountryDTO());

        final boolean exist = productRepository.isExistByNameAndCategoryAndProducer(productDto);

        if (exist) {
            throw new MyException("PRODUCT WITH GIVEN CATEGORY AND PRODUCER EXIST");
        }

        ProducerDto producerDto = productDto.getProducerDTO();

        Product product = productRepository.findByName(productDto).orElse(null);
        Category category = categoryRepository.findByName(productDto.getCategoryDTO()).orElse(null);
        Producer producer = producerRepository.findByName(producerDto).orElse(null);
        Country country = countryRepository.findByName(producerDto.getCountryDTO()).orElse(null);

        if (country == null) {
            country = Mappers.fromCountryDTOToCountry(producerDto.getCountryDTO());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException("CAN NOT ADD COUNTRY IN PRODUCT SERVICE"));
        }

        if (category == null) {
            category = Mappers.fromCategoryDTOtoCategory(productDto.getCategoryDTO());
            category = categoryRepository.addOrUpdate(category).orElseThrow(() -> new MyException("CAN NOT ADD CATEGORY IN PRODUCT SERVICE"));
        }

        if (producer == null) {
            String tradeName = userDataService.getString("Please enter a name of producer trade: ");

            TradeDto tradeDto = TradeDto.builder()
                    .name(tradeName)
                    .build();

            Trade trade = tradeRepository.findByName(tradeDto).orElse(null);
            if (trade == null) {
                trade = Mappers.fromTradeDTOToTrade(tradeDto);
                trade = tradeRepository.addOrUpdate(trade).orElseThrow(() -> new MyException("CAN NOT ADD TRADE IN PRODUCT SERVICE"));
            }

            producer = Mappers.fromProducerDTOToProducer(productDto.getProducerDTO());
            producer.setTrade(trade);
            producer.setCountry(country);
            producer = producerRepository.addOrUpdate(producer).orElseThrow(() -> new MyException("CAN NOT ADD PRODUCER IN PRODUCT SERVICE"));
        }

        if (product == null) {
            product = Mappers.fromProductDTOToProduct(productDto);
        }

        product.setCategory(category);
        product.setProducer(producer);
        productRepository.addOrUpdate(product);
        return Mappers.fromProductToProductDTO(product);
    }

    public Map<Category, Product> findBiggestPriceInCategory(){
        return productRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(Product::getPrice)), proOp -> proOp.orElseThrow(NullPointerException::new))));
    }


}
