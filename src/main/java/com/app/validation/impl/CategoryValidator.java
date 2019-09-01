package com.app.validation.impl;

import com.app.dto.CategoryDto;
import com.app.exceptions.MyException;

import java.util.Map;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class CategoryValidator {

    public void validateCategory(CategoryDto categoryDto){
        if (isNullOrEmpty(categoryDto.getName())){
            throw new MyException("CATEGORY CAN NOT BE NULL OR EMPTY");
        }
        if (!isNameValid(categoryDto)){
            throw new MyException("INVALID CATEGORY NAME");
        }
    }

    private boolean isNameValid(CategoryDto categoryDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return categoryDTO.getName().matches(MODEL_NAME_REGEX);
    }
}
