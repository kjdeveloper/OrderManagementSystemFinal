package com.app.service;

import com.app.dto.ProductDto;
import com.app.exceptions.MyException;
import com.app.model.Category;
import com.app.model.Producer;
import com.app.model.Product;
import com.app.repository.CategoryRepository;
import com.app.repository.ProducerRepository;
import com.app.repository.ProductRepository;
import com.app.repository.impl.CategoryRepositoryImpl;
import com.app.repository.impl.ProducerRepositoryImpl;
import com.app.repository.impl.ProductRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CategoryValidator;
import com.app.validation.impl.ProducerValidator;
import com.app.validation.impl.ProductValidator;

import java.util.Map;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    private final ProducerRepository producerRepository = new ProducerRepositoryImpl();

    private final ProductValidator productValidator = new ProductValidator();
    private final CategoryValidator categoryValidator = new CategoryValidator();
    private final ProducerValidator producerValidator = new ProducerValidator();

    private boolean validateProduct(ProductDto productDTO) {

        Map<String, String> productErrorsMap = productValidator.validate(productDTO);
        if (productValidator.hasErrors()) {
            System.out.println("------PRODUCT VALIDATION ERRORS");
            productErrorsMap.forEach((k, v) -> System.out.println(k + " -> " + v));
            System.out.println("-------------------------------");
        }
        return !productValidator.hasErrors();
    }

    public void addProduct(ProductDto productDTO) {
        productValidator.validateProduct(productDTO);
        categoryValidator.validateCategory(productDTO.getCategoryDTO());
        producerValidator.validateProducer(productDTO.getProducerDTO());

        final boolean exist = productRepository.isExistByNameAndCategoryAndProducer(productDTO);

        if (exist) {
            throw new MyException("PRODUCT WITH GIVEN CATEGORY AND PRODUCER EXIST");
        }

        String productName = productDTO.getName();
        String categoryName = productDTO.getCategoryDTO().getName();
        String producerName = productDTO.getProducerDTO().getName();

        Product product = productRepository.findByName(productName).orElse(null);
        Category category = categoryRepository.findByName(categoryName).orElse(null);
        Producer producer = producerRepository.findByName(producerName).orElse(null);

        if (category == null) {
            category = Mappers.fromCategoryDTOtoCategory(productDTO.getCategoryDTO());
            category = categoryRepository.addOrUpdate(category).orElseThrow(() -> new MyException("CANNOT ADD CATEGORY"));
        }
        if (producer == null) {
            producer = Mappers.fromProducerDTOToProducer(productDTO.getProducerDTO());
            producer = producerRepository.addOrUpdate(producer).orElseThrow(() -> new MyException("CANNOT ADD PRODUCER"));
        }
        if (product == null) {
            product = Mappers.fromProductDTOToProduct(productDTO);
        }

        product.setCategory(category);
        product.setProducer(producer);
        productRepository.addOrUpdate(product);
    }
}
