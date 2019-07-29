package com.app.validation.impl;

import com.app.dto.CategoryDto;
import com.app.exceptions.MyException;
import com.app.validation.generic.AbstractValidator;

import java.util.Map;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class CategoryValidator extends AbstractValidator<CategoryDto> {

    @Override
    public Map<String, String> validate(CategoryDto categoryDTO) {
        if (categoryDTO == null) {
            errors.put("categoryDTO", "null");
        }
        if (!isNameValid(categoryDTO)) {
            errors.put("categoryDTO name", "Name is not valid => " + categoryDTO.getName());
        }
        return errors;
    }

    public void validateCategory(CategoryDto categoryDto){
        if (isNullOrEmpty(categoryDto.getName())){
            throw new MyException("CATEGORY CANNOT BE NULL OR EMPTY");
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
