package com.app.mainmenu;

import com.app.dto.CountryDto;
import com.app.dto.CustomerDto;
import com.app.mainmenu.menu.MenuService;
import com.app.model.enums.EGuarantee;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.ProductRepository;
import com.app.repository.ShopRepository;
import com.app.repository.converters.CustomerConverter;
import com.app.repository.impl.CustomerOrderRepositoryImpl;
import com.app.repository.impl.ProductRepositoryImpl;
import com.app.repository.impl.ShopRepositoryImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class App {
    public static void main(String[] args) {

        MenuService menuService = new MenuService();
        //menuService.service();

      /*  ProductRepository pr = new ProductRepositoryImpl();
        List p = pr.findProductsWithBiggestPriceInCategory();
        p.stream().forEach(p1 -> System.out.println(p1.toString()));

        List p2 = pr.findAllProductsFromSpecificCountryBetweenCustomerAges("POLAND", 18, 30);
        p2.forEach(r -> System.out.println(r));

        List p3 = pr.findAllProductsWithGivenGuarantees(new HashSet<>(Arrays.asList(EGuarantee.SERVICE, EGuarantee.EXCHANGE)));
        p3.forEach(s -> System.out.println(s));

        CustomerOrderRepository cs = new CustomerOrderRepositoryImpl();
        List s5 = cs.findOrdersBetweenDatesAndGivenPrice(LocalDateTime.of(2019, 8, 25, 12, 12, 12), LocalDateTime.of(2019, 8, 30, 12, 12, 30), BigDecimal.valueOf(25));
        System.out.println(s5);*/

        ShopRepository shopRepository = new ShopRepositoryImpl();
        //System.out.println(shopRepository.findAllShopsWithProductsWithCountryDifferentThanShopCountry());

    }
}
