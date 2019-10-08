package com.app;

import com.app.dto.ProductDto;
import com.app.exceptions.MyException;
import com.app.model.Category;
import com.app.model.Product;
import com.app.model.enums.EGuarantee;
import com.app.repository.ProductRepository;
import com.app.service.mapper.Mappers;
import com.app.service.services.ProductService;
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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestProductService {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Find all products")
    public void test1() {

        var category = Category.builder().id(3L).name("HOME").build();
        Mockito.when(productRepository.findAll()).thenReturn(List.of(
                Product.builder().name("ABECADLO").category(category).price(BigDecimal.valueOf(100)).id(1L).build(),
                Product.builder().name("BURAK").category(category).price(BigDecimal.valueOf(10)).id(2L).build()
        ));
        var products = productRepository.findAll();
        Assertions.assertEquals(2, products.size(), "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Find product with biggest price in each category")
    public void test2() {

        var category = Category.builder().id(3L).name("HOME").build();
        var category2 = Category.builder().id(4L).name("GARDEN").build();

        Mockito.when(productRepository.findAll()).thenReturn(List.of(
                Product.builder().name("POTATO").category(category).price(BigDecimal.valueOf(100)).id(1L).build(),
                Product.builder().name("ONION").category(category).price(BigDecimal.valueOf(10)).id(2L).build(),
                Product.builder().name("TABLE").category(category2).price(BigDecimal.valueOf(450)).id(5L).build(),
                Product.builder().name("CHAIR").category(category2).price(BigDecimal.valueOf(125)).id(6L).build()
        ));

        var map = productService.findProductsWithBiggestPriceInCategory();

        var categoryDto = Mappers.fromCategoryToCategoryDto(category);
        var categoryDto2 = Mappers.fromCategoryToCategoryDto(category2);

        Assertions.assertEquals("POTATO", map.get(categoryDto).get().getName(), "TEST 2 FAILED");
        Assertions.assertEquals("TABLE", map.get(categoryDto2).get().getName(), "TEST 2 FAILED");
    }

    @Test
    @DisplayName("Find products with same guarantees")
    public void test3() {

        var category = Category.builder().id(3L).name("HOME").build();
        var category2 = Category.builder().id(4L).name("GARDEN").build();

        var eGuarantees1 = new HashSet<>(List.of(EGuarantee.EXCHANGE, EGuarantee.SERVICE));
        var eGuarantees2 = new HashSet<>(List.of(EGuarantee.SERVICE));
        var eGuarantees3 = new HashSet<>(List.of(EGuarantee.SERVICE, EGuarantee.MONEY_BACK, EGuarantee.HELP_DESK));

        Mockito.when(productRepository.findAll()).thenReturn(List.of(
                Product.builder().name("POTATO").category(category).eGuarantees(eGuarantees1).price(BigDecimal.valueOf(100)).id(1L).build(),
                Product.builder().name("ONION").category(category).eGuarantees(eGuarantees2).price(BigDecimal.valueOf(10)).id(2L).build(),
                Product.builder().name("TABLE").category(category2).eGuarantees(eGuarantees3).price(BigDecimal.valueOf(450)).id(5L).build(),
                Product.builder().name("CHAIR").category(category2).eGuarantees(eGuarantees3).price(BigDecimal.valueOf(125)).id(6L).build()
        ));

        var eGuaranteesToProducts = new HashSet<>(List.of(EGuarantee.SERVICE, EGuarantee.EXCHANGE));
        var products = productService.findAllProductsWithGivenGuarantees(eGuaranteesToProducts);

        var eGuaranteesToProducts2 = new HashSet<>(List.of(EGuarantee.SERVICE));
        var products2 = productService.findAllProductsWithGivenGuarantees(eGuaranteesToProducts2);

        Assertions.assertEquals(1, products.size(), "TEST 3 FAILED");
        Assertions.assertEquals(4, products2.size(), "TEST 3 FAILED");
    }

    @Test
    @DisplayName("Find products with same guarantees exception")
    public void test5() {

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> productService.findAllProductsWithGivenGuarantees(null));

        Assertions.assertEquals("GUARANTEES CANNOT BE NULL", throwable.getExceptionMessage().getMessage(), "TEST 5 FILED");
    }

    @Test
    @DisplayName("add product exception")
    public void test6(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> productService.addProduct(null));

        Assertions.assertEquals("PRODUCT CANNOT BE NULL", throwable.getExceptionMessage().getMessage(), "TEST 6 FAILED");
    }

    @Test
    @DisplayName("add product with wrong name exception")
    public void test7(){

        var productDto = ProductDto.builder().name("strange").price(BigDecimal.valueOf(100)).build();
        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> productService.addProduct(productDto));

        Assertions.assertEquals("PRODUCT NAME IS NOT VALID", throwable.getExceptionMessage().getMessage(), "TEST 7 FAILED");
    }

    @Test
    @DisplayName("add product with wrong price exception")
    public void test8(){

        var productDto = ProductDto.builder().name("STRANGE").price(BigDecimal.valueOf(-10)).build();
        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> productService.addProduct(productDto));

        Assertions.assertEquals("PRODUCT PRICE IS NOT VALID", throwable.getExceptionMessage().getMessage(), "TEST 8 FAILED");
    }
}
