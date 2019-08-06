package com.app.service.services;

import com.app.dto.CategoryDto;
import com.app.exceptions.MyException;
import com.app.model.Category;
import com.app.repository.CategoryRepository;
import com.app.repository.impl.CategoryRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CategoryValidator;

import java.util.Map;

public class CategoryService {

    private CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    private CategoryValidator categoryValidator = new CategoryValidator();

    public void addCategory(CategoryDto categoryDTO) {
        categoryValidator.validateCategory(categoryDTO);

        Category category = categoryRepository.findByName(categoryDTO).orElse(null);

        if (category == null){
            category = Mappers.fromCategoryDTOtoCategory(categoryDTO);
        }else{
            throw new MyException("CATEGORY WITH GIVEN NAME EXIST");
        }

        categoryRepository.addOrUpdate(category);
    }

}
