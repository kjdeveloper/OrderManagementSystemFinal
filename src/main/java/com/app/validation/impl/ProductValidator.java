package com.app.validation.impl;

import com.app.dto.ProductDto;
import com.app.exceptions.MyException;

import java.math.BigDecimal;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class ProductValidator {

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
