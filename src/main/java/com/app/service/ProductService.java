package com.app.service;

import com.app.dto.ProducerDto;
import com.app.dto.ProductDto;
import com.app.dto.TradeDto;
import com.app.exceptions.MyException;
import com.app.model.Category;
import com.app.model.Producer;
import com.app.model.Product;
import com.app.model.Trade;
import com.app.repository.CategoryRepository;
import com.app.repository.ProducerRepository;
import com.app.repository.ProductRepository;
import com.app.repository.TradeRepository;
import com.app.repository.impl.CategoryRepositoryImpl;
import com.app.repository.impl.ProducerRepositoryImpl;
import com.app.repository.impl.ProductRepositoryImpl;
import com.app.repository.impl.TradeRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CategoryValidator;
import com.app.validation.impl.ProducerValidator;
import com.app.validation.impl.ProductValidator;

import java.util.Map;
import java.util.Scanner;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    private final ProducerRepository producerRepository = new ProducerRepositoryImpl();

    private final ProductValidator productValidator = new ProductValidator();
    private final CategoryValidator categoryValidator = new CategoryValidator();
    private final ProducerValidator producerValidator = new ProducerValidator();
    private final TradeRepository tradeRepository = new TradeRepositoryImpl();
    private final TradeService tradeService = new TradeService();

    private final Scanner sc = new Scanner(System.in);

    private boolean validateProduct(ProductDto productDto) {

        Map<String, String> productErrorsMap = productValidator.validate(productDto);
        if (productValidator.hasErrors()) {
            System.out.println("------PRODUCT VALIDATION ERRORS");
            productErrorsMap.forEach((k, v) -> System.out.println(k + " -> " + v));
            System.out.println("-------------------------------");
        }
        return !productValidator.hasErrors();
    }

    public ProductDto addProduct(ProductDto productDto) {
        productValidator.validateProduct(productDto);
        categoryValidator.validateCategory(productDto.getCategoryDTO());
        producerValidator.validateProducer(productDto.getProducerDTO());

        final boolean exist = productRepository.isExistByNameAndCategoryAndProducer(productDto);

        if (exist) {
            throw new MyException("PRODUCT WITH GIVEN CATEGORY AND PRODUCER EXIST");
        }

        ProducerDto producerDto = productDto.getProducerDTO();

        Product product = productRepository.findByName(productDto).orElse(null);
        Category category = categoryRepository.findByName(productDto.getCategoryDTO()).orElse(null);
        Producer producer = producerRepository.findByName(producerDto).orElse(null);

        if (category == null) {
            category = Mappers.fromCategoryDTOtoCategory(productDto.getCategoryDTO());
            category = categoryRepository.addOrUpdate(category).orElseThrow(() -> new MyException("CANNOT ADD CATEGORY"));
        }
        if (producer == null) {
            System.out.println("Please enter a name of producers trade: ");
            String t = sc.next();
            TradeDto tradeDto = TradeDto.builder().name(t).build();
            tradeService.addTrade(tradeDto);
            Trade trade = Mappers.fromTradeDTOToTrade(tradeDto);
            producer = Mappers.fromProducerDTOToProducer(productDto.getProducerDTO());
            producer.setTrade(trade);
            producer = producerRepository.addOrUpdate(producer).orElseThrow(() -> new MyException("CANNOT ADD PRODUCER"));
        }
        if (product == null) {
            product = Mappers.fromProductDTOToProduct(productDto);
        }

        product.setCategory(category);
        product.setProducer(producer);
        productRepository.addOrUpdate(product);
        return Mappers.fromProductToProductDTO(product);
    }
}
