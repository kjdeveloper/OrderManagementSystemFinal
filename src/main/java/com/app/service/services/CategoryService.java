package com.app.service.services;

import com.app.dto.CategoryDto;
import com.app.exceptions.MyException;
import com.app.model.Category;
import com.app.repository.CategoryRepository;
import com.app.repository.impl.CategoryRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CategoryValidator;

public class CategoryService {

    private CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    private CategoryValidator categoryValidator = new CategoryValidator();

    public CategoryDto addCategory(CategoryDto categoryDTO) {
        categoryValidator.validateCategory(categoryDTO);

        Category category = categoryRepository.findByName(categoryDTO.getName()).orElse(null);

        if (category == null){
            category = Mappers.fromCategoryDtoToCategory(categoryDTO);
        }else{
            throw new MyException("CATEGORY WITH GIVEN NAME EXIST");
        }

        categoryRepository.addOrUpdate(category).orElseThrow(() -> new MyException("CAN NOT ADD CATEGORY IN CATEGORY SERVICE"));
        return Mappers.fromCategoryToCategoryDto(category);
    }

}
