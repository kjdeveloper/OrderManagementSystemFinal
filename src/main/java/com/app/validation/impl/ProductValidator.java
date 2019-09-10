package com.app.validation.impl;

import com.app.dto.ProductDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;

import java.math.BigDecimal;


public class ProductValidator {

    public void validateProduct(ProductDto productDto){
        if(productDto == null){
            throw new MyException(ExceptionCode.PRODUCT, "PRODUCT CAN NOT BE NULL");
        }
        if (!isNameValid(productDto)){
            throw new MyException(ExceptionCode.PRODUCT, "PRODUCT NAME IS NOT VALID");
        }
        if (!isPriceValid(productDto)){
            throw new MyException(ExceptionCode.PRODUCT, "PRODUCT PRICE IS NOT VALID");
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
