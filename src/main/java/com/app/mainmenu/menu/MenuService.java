package com.app.mainmenu.menu;

import com.app.dto.*;
import com.app.exceptions.MyException;
import com.app.model.enums.EGuarantee;
import com.app.model.enums.EPayment;
import com.app.repository.converters.*;
import com.app.repository.generic.DbConnection;
import com.app.service.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MenuService {

    private final UserDataService userDataService = new UserDataService();
    private final DbConnection dbConnection = DbConnection.getInstance();

    private final CustomerService customerService = new CustomerService();
    private final CustomerOrderService customerOrderService = new CustomerOrderService();
    private final ShopService shopService = new ShopService();
    private final ProducerService producerService = new ProducerService();
    private final ProductService productService = new ProductService();
    private final StockService stockService = new StockService();
    private final ErrorService errorService = new ErrorService();

    private final CategoryDtoConverter categoryDtoConverter = new CategoryDtoConverter();
    private final CustomerDtoConverter customerDtoConverter = new CustomerDtoConverter();
    private final CustomerOrderDtoConverter customerOrderDtoConverter = new CustomerOrderDtoConverter();
    private final ProducerDtoConverter producerDtoConverter = new ProducerDtoConverter();
    private final ProductDtoConverter productDtoConverter = new ProductDtoConverter();
    private final ShopDtoConverter shopDtoConverter = new ShopDtoConverter();
    private final StockDtoConverter stockDtoConverter = new StockDtoConverter();
    private final TradeDtoConverter tradeDtoConverter = new TradeDtoConverter();

    public MenuService() {
    }

    private void menu() {
        System.out.println("\nMENU");
        System.out.println("\n=========== Adding methods ==============");
        System.out.println("1. Add new customer");
        System.out.println("2. Add new shop");
        System.out.println("3. Add new producer");
        System.out.println("4. Add new product");
        System.out.println("5. Add new position in stock");
        System.out.println("6. Add new customer order");
        System.out.println("\n=========== Downloads methods ===========");
        System.out.println("7. Get products with biggest price in each category");
        System.out.println("8. Get products with the same country as given and age range");
        System.out.println("9. Get products with same guarantees as given ");
        System.out.println("10. Get shops with products from different country than shop");
        System.out.println("11. Get producers with given trade name and more products produced than specified by the user");
        System.out.println("12. Get orders from a specified period of time and total price greater than that given by the user");
        System.out.println("13. Get products by customer name, surname and country. Grouped by producers");
        System.out.println("14. Get customers which ordered at least one product produced in the same country as the customer. " +
                "additionally display all the product from different country than customer country");
        System.out.println("=========================================");
        System.out.println("0. EXIT");
    }

    public void service() {
        int action;
        do {
            try {
                menu();
                action = userDataService.getInt("Choose option: ");
                switch (action) {
                    case 1:
                        CustomerDto customerDto = option1();
                        System.out.println(customerDtoConverter.toJson(customerDto) + "\n ADDED.");
                        break;
                    case 2:
                        ShopDto shopDto = option2();
                        System.out.println(shopDtoConverter.toJson(shopDto) + "\n ADDED.");
                        break;
                    case 3:
                        ProducerDto producerDto = option3();
                        System.out.println(producerDtoConverter.toJson(producerDto) + "\n ADDED.");
                        break;
                    case 4:
                        Set<EGuarantee> eGuarantees = new HashSet<>(Arrays.asList(
                                EGuarantee.EXCHANGE,
                                EGuarantee.SERVICE
                        ));
                        ProductDto productDtoAdded = option4(eGuarantees);
                        System.out.println(productDtoConverter.toJson(productDtoAdded) + "\n ADDED.");
                        break;
                    case 5:
                        StockDto stockDto = option5();
                        System.out.println(stockDtoConverter.toJson(stockDto) + "\n ADDED.");
                        break;
                    case 6:
                        Set<EPayment> ePayments = new HashSet<>(Arrays.asList(
                                EPayment.CASH, EPayment.CARD
                        ));
                        double discount = 0.75;

                        CustomerOrderDto customerOrderDto = option6(ePayments, discount);
                        System.out.println(customerOrderDtoConverter.toJson(customerOrderDto));
                        break;

                    //============================DOWNLOAD DATA METHODS===============================
                    case 7:
                        List<ProductDto> biggestPriceInEachCategory = option7();
                        biggestPriceInEachCategory.forEach(p ->
                                System.out.println(p.getName() +
                                        ", price: " + p.getPrice() +
                                        ", category: " + p.getCategoryDto().getName() +
                                        ", producer: " + p.getProducerDto().getName() +
                                        ", from " + p.getProducerDto().getCountryDto().getName() +
                                        " ordered " + customerOrderService.customerOrdersWithSpecificProduct(p.getName()).size() + " times"
                                ));
                        break;
                    case 8:
                        String country = userDataService.getString("Please, enter a name of country: ");
                        int ageFrom = userDataService.getInt("Please, enter a minimum age for the customers you want to see: ");
                        int ageTo = userDataService.getInt("Please, enter a maximum age for the customers you want to see: ");
                        List<ProductDto> products = option8(country, ageFrom, ageTo);

                        for (ProductDto productDto : products) {
                            System.out.println(productDtoConverter.toJson(productDto));
                        }
                        products.forEach(productDto -> System.out.println(productDto.getName() +
                                ", price: " + productDto.getPrice() +
                                ", category: " + productDto.getCategoryDto().getName() +
                                ", producer: " + productDto.getProducerDto().getName() +
                                ", from: " + productDto.getProducerDto().getCountryDto().getName()));
                        break;
                    case 9:
                        Set<EGuarantee> eGuaranteesForProductWithSameComponents = new HashSet<>(Arrays.asList(
                                EGuarantee.EXCHANGE,
                                EGuarantee.SERVICE
                        ));

                        Set<ProductDto> productWithSameGuaranteeComponents = option9(eGuaranteesForProductWithSameComponents);
                        System.out.println(productWithSameGuaranteeComponents);
                        break;
                    case 10:
                        List<ShopDto> shops = option10();
                        break;
                    case 11:
                        String tradeName = userDataService.getString("Please, enter a trade name; ");
                        Long quantity = (long) userDataService.getInt("Please, enter a quantity: ");
                        List<ProducerDto> producers = option11(tradeName, quantity);
                        System.out.println(producers);
                        break;
                    case 12:
                        LocalDate dateFrom = LocalDate.parse(userDataService.getString("Please, enter start date: (FORMAT: YYYY-mm-dd)"));
                        LocalDate dateTo = LocalDate.parse(userDataService.getString("Please, enter end date: (FORMAT: YYYY-mm-dd)"));
                        BigDecimal price = userDataService.getBigDecimal("Please, enter the price at which you want to filter orders: ");
                        List<CustomerOrderDto> listOfOrders = option12(dateFrom, dateTo, price);
                        System.out.println(listOfOrders);
                        break;
                    case 13:
                        String customerName = userDataService.getString("Please, enter a customer name: ");
                        String customerSurname = userDataService.getString("Please, enter a customer surname: ");
                        String countryName = userDataService.getString("Please, enter a country name: ");
                        List<ProductDto> mapOfProductWithGivenCustomerGroupedByProducer = option13(customerName, customerSurname, countryName);
                        mapOfProductWithGivenCustomerGroupedByProducer.forEach(p -> System.out.println(p));
                        break;
                    case 14:
                        Map<CountryDto, List<String>> map = option14();
                        System.out.println(map);
                        break;
                    case 0:
                        dbConnection.close();
                        System.out.println("Bye Bye");
                        return;
                }
            } catch (MyException me) {
                errorService.addError(me.getExceptionMessage().getExceptionCode().toString()+";" + me.getExceptionMessage().getMessage());
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
                .countryDto(CountryDto.builder().name(country).build())
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
                .countryDto(countryDto)
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
                .tradeDto(tradeDto)
                .countryDto(countryDto)
                .build();

        return producerService.addProducer(producerDto);
    }

    private ProductDto option4(Set<EGuarantee> eGuarantees) {
        String name = userDataService.getString("Please enter a name of product: ");
        String category = userDataService.getString("Please enter a product category: ");
        BigDecimal price = userDataService.getBigDecimal("Please enter a product price: ");
        String producerName = userDataService.getString("Please enter a name of the product producer: ");
        String producerCountry = userDataService.getString("Please enter a producer's country: ");

        ProductDto productDto = ProductDto.builder()
                .name(name)
                .price(price)
                .categoryDto(CategoryDto.builder()
                        .name(category)
                        .build())
                .producerDto(ProducerDto.builder()
                        .name(producerName)
                        .countryDto(CountryDto.builder()
                                .name(producerCountry)
                                .build())
                        .build())
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

        return stockService.addProductToStock(productName, categoryName, shopName, countryName, quantity);
    }

    private CustomerOrderDto option6(Set<EPayment> ePayments, double discount) {
        String customerName = userDataService.getString("Please enter customer name: ");
        String customerSurname = userDataService.getString("Please enter customer surname: ");
        String countryName = userDataService.getString("Please enter the country: ");

        String productName = userDataService.getString("Please enter product name: ");
        String categoryName = userDataService.getString("Please enter product category name: ");

        int quantity = userDataService.getInt("Please enter the quantity of product you want to buy: ");
        LocalDateTime ldt = LocalDateTime.now();

        CustomerOrderDto customerOrderDto = CustomerOrderDto.builder()
                .date(ldt)
                .quantity(quantity)
                .discount(discount)
                .customerDto(CustomerDto.builder()
                        .name(customerName)
                        .surname(customerSurname)
                        .countryDto(CountryDto.builder()
                                .name(countryName)
                                .build())
                        .build())
                .productDto(ProductDto.builder()
                        .name(productName)
                        .categoryDto(CategoryDto.builder()
                                .name(categoryName)
                                .build())
                        .build())
                .ePayments(ePayments)
                .build();

        return customerOrderService.addCustomerOrder(customerOrderDto);
    }


    private List<ProductDto> option7() {
        return productService.findProductsWithBiggestPriceInCategory();
    }

    private List<ProductDto> option8(String country, int ageTo, int ageFrom) {
        return productService.findAllProductsFromSpecificCountryBetweenCustomerAges(country, ageTo, ageFrom);
    }

    private Set<ProductDto> option9(Set<EGuarantee> eGuarantees) {
        return productService.findAllProductsWithGivenGuarantees(eGuarantees);
    }

    private List<ShopDto> option10() {
        return shopService.findAllShopsWithProductsWithCountryDifferentThanShopCountry();
    }

    private List<ProducerDto> option11(String tradeName, Long quantity) {
        return producerService.findProducerWithGivenBrandAndTheBiggerQuantityProducedThanGiven(tradeName, quantity);
    }

    private List<CustomerOrderDto> option12(LocalDate dateFrom, LocalDate dateTo, BigDecimal price) {
        return customerOrderService.findOrdersBetweenDatesAndGivenPrice(dateFrom, dateTo, price);
    }

    private List<ProductDto> option13(String customerName, String customerSurname, String countryName) {
        return customerOrderService.findProductsByCustomerAndHisCountry(customerName, customerSurname, countryName);
    }

    private Map<CountryDto, List<String>> option14() {
        return customerService.findCustomersWhoOrderedProductWithSameCountryAsTheir();
    }

}
