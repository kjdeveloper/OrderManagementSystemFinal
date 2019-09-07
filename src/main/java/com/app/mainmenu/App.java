package com.app.mainmenu;

import com.app.mainmenu.menu.MenuService;
import com.app.model.Country;
import com.app.model.Customer;
import com.app.model.CustomerOrder;
import com.app.model.Product;
import com.app.model.enums.EGuarantee;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.ProductRepository;
import com.app.repository.ShopRepository;
import com.app.repository.impl.CustomerOrderRepositoryImpl;
import com.app.repository.impl.ProductRepositoryImpl;
import com.app.repository.impl.ShopRepositoryImpl;
import com.app.service.services.CustomerOrderService;
import com.app.service.services.ErrorService;
import com.app.service.services.ProductService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class App {
    public static void main(String[] args) {

        MenuService menuService = new MenuService();
        menuService.service();

     /*   System.out.println("=============================1==================================");
        ProductRepository pr = new ProductRepositoryImpl();
        List p = pr.findProductsWithBiggestPriceInCategory();
        p.stream().forEach(p1 -> System.out.println(p1.toString()));

        System.out.println("=============================2==================================");
        List p2 = pr.findAllProductsFromSpecificCountryBetweenCustomerAges("POLAND", 18, 30);
        p2.forEach(r -> System.out.println(r));

        System.out.println("=============================3==================================");
        ProductService ps = new ProductService();
        List p3 = ps.findAllProductsWithGivenGuarantees(new HashSet<>(Arrays.asList(EGuarantee.SERVICE, EGuarantee.EXCHANGE)));
        p3.forEach(s -> System.out.println(s));

        System.out.println("=============================4==================================");
        CustomerOrderRepository cs = new CustomerOrderRepositoryImpl();
        List s5 = cs.findOrdersBetweenDatesAndGivenPrice(LocalDate.of(2019, 8, 25), LocalDate.of(2019, 8, 30), new BigDecimal(25));
        System.out.println(s5);

        System.out.println("=============================5==================================");
        ShopRepository shopRepository = new ShopRepositoryImpl();
        System.out.println(shopRepository.findAllShopsWithProductsWithCountryDifferentThanShopCountry());
*/
    }
}
