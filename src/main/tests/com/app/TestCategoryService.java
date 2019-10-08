package com.app;

import com.app.dto.CategoryDto;
import com.app.exceptions.MyException;
import com.app.model.Category;
import com.app.repository.CategoryRepository;
import com.app.service.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestCategoryService {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("Find all")
    public void test1(){

        var category1 = Category.builder().id(1L).name("CATEGORY1").build();
        var category2 = Category.builder().id(2L).name("CATEGORY2").build();

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        var categories = categoryRepository.findAll();
        Assertions.assertEquals(2, categories.size(), "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Add category with wrong name exception")
    public void test2(){

        var categoryDto = CategoryDto.builder().name("something").build();

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> categoryService.addCategory(categoryDto)
        );
        Assertions.assertEquals("INVALID CATEGORY NAME", throwable.getExceptionMessage().getMessage(), "TEST 2 FAILED");
    }

    @Test
    @DisplayName("Add category with null value")
    public void test3(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> categoryService.addCategory(null)
        );

        Assertions.assertEquals("CATEGORY CANNOT BE NULL OR EMPTY", throwable.getExceptionMessage().getMessage(), "TEST 3 FAILED");
    }
}
