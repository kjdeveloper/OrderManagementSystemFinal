package com.app.mainmenu.menu;

import com.app.dto.*;
import com.app.model.enums.EGuarantee;
import com.app.repository.generic.DbConnection;
import com.app.service.services.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MenuService {

    private final UserDataService userDataService = new UserDataService();
    private final DbConnection dbConnection = DbConnection.getInstance();

    private final CustomerService customerService = new CustomerService();
    private final ShopService shopService = new ShopService();
    private final ProducerService producerService = new ProducerService();
    private final ProductService productService = new ProductService();

    private Scanner sc = new Scanner(System.in);

    public MenuService() {
    }

    private void menu() {
        System.out.println("\nMENU");
        System.out.println("\n========== Adding methods ==========");
        System.out.println("1.Add new customer");
        System.out.println("2.Add new shop");
        System.out.println("3.Add new producer");
        System.out.println("4.Add new product");
        System.out.println("5.Add new position in stock");
        System.out.println("6.Add new customer order");
        System.out.println("\n========== Downloads methods ==========");
        System.out.println("8.");
        System.out.println("9.");
        System.out.println("10.");
        System.out.println("11.");
        System.out.println("12.");
        System.out.println("13.");
        System.out.println("14.");
        System.out.println("15.");
        System.out.println(" ========================================");
        System.out.println("0. EXIT");
    }

    public void service() {
        int action;
        do {
            menu();
            action = userDataService.getInt("Choose option: ");
            switch (action) {
                case 1:
                    CustomerDto customerDto = option1();
                    System.out.println(customerDto + " ADDED.");
                    break;
                case 2:
                    ShopDto shopDto = option2();
                    System.out.println(shopDto + " ADDED.");
                    break;
                case 3:
                    ProducerDto producerDto = option3();
                    System.out.println(producerDto + " ADDED.");
                    break;
                case 4:
                    Set<EGuarantee> eGuarantees = new HashSet<>(Arrays.asList(
                            EGuarantee.EXCHANGE,
                            EGuarantee.SERVICE
                    ));

                    ProductDto productDto = option4(eGuarantees);
                    System.out.println(productDto + " ADDED.");
                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:

                    break;
                case 8:

                    break;
                case 9:

                    break;
                case 10:

                    break;
                case 11:

                    break;
                case 0:
                    dbConnection.close();
                    System.out.println("Bye Bye");
                    return;
            }
        } while (true);
    }

    private CustomerDto option1() {
        String name = userDataService.getString("Please enter a name of customer: ");
        String surname = userDataService.getString("Please enter a surname of customer: ");
        int age = userDataService.getInt("Please enter the age of the customer: ");
        String country = userDataService.getString("Please enter the customer's country: ");

        CustomerDto customerDto = CustomerDto.builder()
                .name(name)
                .surname(surname)
                .age(age)
                .countryDTO(CountryDto.builder().name(country).build())
                .build();

        return customerService.addCustomer(customerDto);
    }

    private ShopDto option2() {
        String name = userDataService.getString("Please enter a name of shop: ");
        String country = userDataService.getString("Please enter a shop country: ");

        CountryDto countryDto = CountryDto.builder()
                .name(country)
                .build();

        ShopDto shopDto = ShopDto.builder()
                .name(name)
                .countryDTO(countryDto)
                .build();

        return shopService.addShop(shopDto);
    }

    private ProducerDto option3() {
        String name = userDataService.getString("Please enter a name of producer: ");
        String country = userDataService.getString("Please enter a producer's country: ");
        String trade = userDataService.getString("Please enter a name of trade: ");

        CountryDto countryDto = CountryDto.builder()
                .name(country)
                .build();

        TradeDto tradeDto = TradeDto.builder()
                .name(trade)
                .build();

        ProducerDto producerDto = ProducerDto.builder()
                .name(name)
                .tradeDTO(tradeDto)
                .countryDTO(countryDto)
                .build();

        return producerService.addProducer(producerDto);
    }

    private ProductDto option4(Set<EGuarantee> eGuarantees) {
        String name = userDataService.getString("Please enter a name of product: ");
        String category = userDataService.getString("Please enter a product category: ");
        BigDecimal price = userDataService.getBigDecimal("Please enter a product price: ");
        String producerName = userDataService.getString("Please enter a name of the product producer: ");
        String producerCountry = userDataService.getString("Please enter a producer's country: ");

        CategoryDto categoryDto = CategoryDto.builder()
                .name(category)
                .build();

        ProducerDto producerDto = ProducerDto.builder()
                .name(producerName)
                .countryDTO(CountryDto.builder()
                        .name(producerCountry)
                        .build())
                .build();

        ProductDto productDto = ProductDto.builder()
                .name(name)
                .price(price)
                .categoryDTO(categoryDto)
                .producerDTO(producerDto)
                .eGuarantees(eGuarantees)
                .build();

        return productService.addProduct(productDto);
    }

    private StockDto option5() {
        String productName = userDataService.getString("Please enter a name of product: ");
        String categoryName = userDataService.getString("Please enter a name of category: ");
        String shopName = userDataService.getString("Please enter a shop name: ");
        String countryName = userDataService.getString("Please enter a shop country: ");
        int quantity = userDataService.getInt("Please enter a quantity: ");

        CountryDto countryDto = CountryDto.builder()
                .name(categoryName)
                .build();

        ShopDto shopDto = ShopDto.builder()
                .name(shopName)
                .countryDTO(countryDto)
                .build();
        /*
        ProductDto productDto = ProductDto.builder()
                .
                .build()
        */
        StockDto stockDto = StockDto.builder()

                .build();
        return stockDto;
    }

}
