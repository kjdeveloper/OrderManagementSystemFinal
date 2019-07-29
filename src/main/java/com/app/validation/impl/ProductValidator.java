package com.app.validation.impl;

import com.app.dto.ProductDto;
import com.app.exceptions.MyException;
import com.app.validation.generic.AbstractValidator;

import java.math.BigDecimal;
import java.util.Map;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class ProductValidator extends AbstractValidator<ProductDto> {

    @Override
    public Map<String, String> validate(ProductDto productDTO) {

        if (productDTO == null) {
            errors.put("productDTO", "null");
        }
        if (!isNameValid(productDTO)) {
            errors.put("productDTO name", "Name is not valid => " + productDTO.getName());
        }
        if (!isPriceValid(productDTO)) {
            errors.put("productDTO price", "Price is not valid => " + productDTO.getPrice());
        }

        return errors;
    }

    public void validateProduct(ProductDto productDto){
        if(isNullOrEmpty(productDto.getName())){
            throw new MyException("PRODUCT CANNOT BE NULL OR EMPTY");
        }
        if (!isNameValid(productDto)){
            throw new MyException("PRODUCT NAME IS NOT VALID");
        }
        if (!isPriceValid(productDto)){
            throw new MyException("PRODUCT PRICE IS NOT VALID");
        }
    }


    private boolean isNameValid(ProductDto productDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return productDTO.getName().matches(MODEL_NAME_REGEX);
    }

    private boolean isPriceValid(ProductDto productDTO) {
        return productDTO.getPrice().compareTo(BigDecimal.ZERO) > 0;
    }
}
