package com.app.mainmenu;

import com.app.mainmenu.menu.MenuService;
import com.app.model.enums.EGuarantee;
import com.app.repository.ProductRepository;
import com.app.repository.impl.ProductRepositoryImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class App {
    public static void main(String[] args) {

        MenuService menuService = new MenuService();
        //menuService.service();

        ProductRepository pr = new ProductRepositoryImpl();
        List p = pr.findProductsWithBiggestPriceInCategory();
        p.stream().forEach(p1 -> System.out.println(p1.toString()));

        List p2 = pr.findAllProductsFromSpecificCountryBetweenCustomerAges("POLAND", 18, 30);
        p2.forEach(r -> System.out.println(r));

        List p3 = pr.findAllProductsWithGivenGuarantees(new HashSet<>(Arrays.asList(EGuarantee.SERVICE, EGuarantee.EXCHANGE)));
        p3.forEach(s -> System.out.println(s));




    }
}
