package com.app.validation.impl;

import com.app.dto.CategoryDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;

import java.util.Map;


public class CategoryValidator {

    public void validateCategory(CategoryDto categoryDto){
        if (categoryDto == null){
            throw new MyException(ExceptionCode.CATEGORY, "CATEGORY CANNOT BE NULL OR EMPTY");
        }
        if (!isNameValid(categoryDto)){
            throw new MyException(ExceptionCode.CATEGORY, "INVALID CATEGORY NAME");
        }
    }

    private boolean isNameValid(CategoryDto categoryDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return categoryDTO.getName().matches(MODEL_NAME_REGEX);
    }
}
