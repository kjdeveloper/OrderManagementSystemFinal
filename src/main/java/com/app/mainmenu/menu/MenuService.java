package com.app.mainmenu.menu;

import com.app.dto.*;
import com.app.exceptions.MyException;
import com.app.model.enums.EGuarantee;
import com.app.model.enums.EPayment;
import com.app.repository.*;
import com.app.repository.converters.*;
import com.app.repository.generic.DbConnection;
import com.app.repository.impl.*;
import com.app.service.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MenuService {

    private final UserDataService userDataService = new UserDataService();
    private final DbConnection dbConnection = DbConnection.getInstance();

    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();
    private final CustomerRepository customerRepository = new CustomerRepositoryImpl();
    private final CustomerOrderRepository customerOrderRepository = new CustomerOrderRepositoryImpl();
    private final ErrorRepository errorRepository = new ErrorRepositoryImpl();
    private final ProducerRepository producerRepository = new ProducerRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final ShopRepository shopRepository = new ShopRepositoryImpl();
    private final StockRepository stockRepository = new StockRepositoryImpl();
    private final TradeRepository tradeRepository = new TradeRepositoryImpl();

    private final CustomerService customerService = new CustomerService(customerRepository, countryRepository);
    private final CustomerOrderService customerOrderService = new CustomerOrderService(customerOrderRepository, stockRepository, customerRepository, productRepository);
    private final ShopService shopService = new ShopService(shopRepository, countryRepository, stockRepository);
    private final ProducerService producerService = new ProducerService(producerRepository, countryRepository, tradeRepository, stockRepository);
    private final ProductService productService = new ProductService(productRepository, categoryRepository, producerRepository);
    private final StockService stockService = new StockService(stockRepository, productRepository, shopRepository);
    private final ErrorService errorService = new ErrorService(errorRepository);

    private final CustomerDtoConverter customerDtoConverter = new CustomerDtoConverter();
    private final CustomerOrderDtoConverter customerOrderDtoConverter = new CustomerOrderDtoConverter();
    private final ProducerDtoConverter producerDtoConverter = new ProducerDtoConverter();
    private final ProductDtoConverter productDtoConverter = new ProductDtoConverter();
    private final ShopDtoConverter shopDtoConverter = new ShopDtoConverter();
    private final StockDtoConverter stockDtoConverter = new StockDtoConverter();
    private final ProductsDtoConverter productsDtoConverter = new ProductsDtoConverter();
    private final ShopsDtoConverter shopsDtoConverter = new ShopsDtoConverter();
    private final ProducersDtoConverter producersDtoConverter = new ProducersDtoConverter();
    private final CustomerOrdersDtoConverter customerOrdersDtoConverter = new CustomerOrdersDtoConverter();
    private final CategoryDtoConverter categoryDtoConverter = new CategoryDtoConverter();

    public MenuService() {
    }

    private void menu() {
        System.out.println("\nMENU");
        System.out.println("\n=========== Adding methods ==============");
        System.out.println("1. Add new customer.");
        System.out.println("2. Add new shop.");
        System.out.println("3. Add new producer.");
        System.out.println("4. Add new product.");
        System.out.println("5. Add new position in stock.");
        System.out.println("6. Add new customer order.");
        System.out.println("\n=========== Downloads methods ===========");
        System.out.println("7. Get products with biggest price in each category.");
        System.out.println("8. Get products with the same country as given and age range.");
        System.out.println("9. Get products with same guarantees as given.");
        System.out.println("10. Get shops with products from different country than shop.");
        System.out.println("11. Get producers with given trade name and more products produced than specified by the user.");
        System.out.println("12. Get orders from a specified period of time and total price greater than that given by the user.");
        System.out.println("13. Get products by customer name, surname and country. Grouped by producers.");
        System.out.println("14. Get customers which ordered at least one product produced in the same country as the customer " +
                "additionally display all the product from different country than customer country.");
        System.out.println("15. Error statistics.");
        System.out.println("=========================================");
        System.out.println("0. EXIT.");
    }

    public void service() {
        int action;
        do {
            try {
                menu();
                action = userDataService.getInt("Choose option: ");
                switch (action) {
                    case 1:
                        var customerDto = option1();
                        System.out.println(customerDtoConverter.toJsonView(customerDto) + "\nADDED.");
                        break;
                    case 2:
                        var shopDto = option2();
                        System.out.println(shopDtoConverter.toJsonView(shopDto) + "\nADDED.");
                        break;
                    case 3:
                        var producerDto = option3();
                        System.out.println(producerDtoConverter.toJsonView(producerDto) + "\nADDED.");
                        break;
                    case 4:
                        Set<EGuarantee> eGuarantees = new HashSet<>(Arrays.asList(
                                EGuarantee.EXCHANGE,
                                EGuarantee.SERVICE
                        ));
                        var productDtoAdded = option4(eGuarantees);
                        System.out.println(productDtoConverter.toJsonView(productDtoAdded) + "\nADDED.");
                        break;
                    case 5:
                        var stockDto = option5();
                        System.out.println(stockDtoConverter.toJsonView(stockDto) + "\nADDED.");
                        break;
                    case 6:
                        Set<EPayment> ePayments = new HashSet<>(Arrays.asList(
                                EPayment.CASH, EPayment.CARD, EPayment.MONEY_TRANSFER
                        ));
                        var discount = 0.6;
                        var customerOrderDto = option6(ePayments, discount);
                        System.out.println(customerOrderDtoConverter.toJsonView(customerOrderDto) + "\nADDED.");
                        break;

                    //============================DOWNLOAD DATA METHODS===============================
                    case 7:
                        Map<CategoryDto, Optional<ProductDto>> biggestPriceInEachCategory = option7();
                        biggestPriceInEachCategory.forEach((k, v) -> System.out.println(categoryDtoConverter.toJsonView(k) + " => " + productDtoConverter.toJsonView(v.get())));
                        break;
                    case 8:
                        var country = userDataService.getString("Please, enter a name of country: ");
                        var ageFrom = userDataService.getInt("Please, enter a minimum age for the customers you want to see: ");
                        var ageTo = userDataService.getInt("Please, enter a maximum age for the customers you want to see: ");
                        List<ProductDto> products = option8(country, ageFrom, ageTo);

                        System.out.println(productsDtoConverter.toJsonView(products));

                        break;
                    case 9:
                        Set<EGuarantee> eGuaranteesForProductWithSameComponents = new HashSet<>(Arrays.asList(
                                EGuarantee.EXCHANGE, EGuarantee.SERVICE
                        ));

                        List<ProductDto> productWithSameGuaranteeComponents = option9(eGuaranteesForProductWithSameComponents);
                        System.out.println(productsDtoConverter.toJsonView(productWithSameGuaranteeComponents));
                        break;
                    case 10:
                        Set<ShopDto> shops = option10();
                        System.out.println(shopsDtoConverter.toJsonView(shops));
                        break;
                    case 11:
                        var tradeName = userDataService.getString("Please, enter a trade name; ");
                        var quantity = (long) userDataService.getInt("Please, enter a quantity: ");
                        Set<ProducerDto> producers = option11(tradeName, quantity);
                        System.out.println(producersDtoConverter.toJsonView(producers));
                        break;
                    case 12:
                        var dateFrom = LocalDate.parse(userDataService.getString("Please, enter start date: (FORMAT: YYYY-mm-dd)"));
                        var dateTo = LocalDate.parse(userDataService.getString("Please, enter end date: (FORMAT: YYYY-mm-dd)"));
                        var price = userDataService.getBigDecimal("Please, enter the price at which you want to filter orders: ");
                        List<CustomerOrderDto> listOfOrders = option12(dateFrom, dateTo, price);
                        System.out.println(customerOrdersDtoConverter.toJsonView(listOfOrders));
                        break;
                    case 13:
                        var customerName = userDataService.getString("Please, enter a customer name: ");
                        var customerSurname = userDataService.getString("Please, enter a customer surname: ");
                        var countryName = userDataService.getString("Please, enter a country name: ");
                        Map<ProducerDto, List<ProductDto>> mapOfProductWithGivenCustomerGroupedByProducer = option13(customerName, customerSurname, countryName);
                        mapOfProductWithGivenCustomerGroupedByProducer.forEach((k, v) -> System.out.println(producerDtoConverter.toJsonView(k) + " => " + productsDtoConverter.toJsonView(v)));
                        break;
                    case 14:
                        Set<CustomerDto> customerDtoList = option14();

                        for (CustomerDto c : customerDtoList) {
                            System.out.println(c + " => " + customerOrderService.getProductQuantityWithDifferentCountry(c.getId()));
                        }
                        break;
                    case 15:
                        option15();
                        break;
                    case 0:
                        dbConnection.close();
                        System.out.println("Bye Bye");
                        return;
                }
            } catch (MyException me) {
                errorService.addError(me.getExceptionMessage().getExceptionCode().toString() + ";" + me.getExceptionMessage().getMessage());
            }
        } while (true);
    }

    private CustomerDto option1() {
        var name = userDataService.getString("Please enter a name of customer: ");
        var surname = userDataService.getString("Please enter a surname of customer: ");
        var age = userDataService.getInt("Please enter the age of the customer: ");
        var country = userDataService.getString("Please enter the customer's country: ");

        var customerDto = CustomerDto.builder()
                .name(name)
                .surname(surname)
                .age(age)
                .countryDto(CountryDto.builder().name(country).build())
                .build();

        return customerService.addCustomer(customerDto);
    }

    private ShopDto option2() {
        var name = userDataService.getString("Please enter a name of shop: ");
        var country = userDataService.getString("Please enter a shop country: ");

        var countryDto = CountryDto.builder()
                .name(country)
                .build();

        var shopDto = ShopDto.builder()
                .name(name)
                .countryDto(countryDto)
                .build();

        return shopService.addShop(shopDto);
    }

    private ProducerDto option3() {
        var name = userDataService.getString("Please enter a name of producer: ");
        var country = userDataService.getString("Please enter a producer's country: ");
        var trade = userDataService.getString("Please enter a name of trade: ");

        var countryDto = CountryDto.builder()
                .name(country)
                .build();

        var tradeDto = TradeDto.builder()
                .name(trade)
                .build();

        var producerDto = ProducerDto.builder()
                .name(name)
                .tradeDto(tradeDto)
                .countryDto(countryDto)
                .build();

        return producerService.addProducer(producerDto);
    }

    private ProductDto option4(Set<EGuarantee> eGuarantees) {
        var name = userDataService.getString("Please enter a name of product: ");
        var category = userDataService.getString("Please enter a product category: ");
        var price = userDataService.getBigDecimal("Please enter a product price: ");
        var producerName = userDataService.getString("Please enter a name of the product producer: ");
        var producerCountry = userDataService.getString("Please enter a producer's country: ");

        var productDto = ProductDto.builder()
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
        var productName = userDataService.getString("Please enter a name of product: ");
        var categoryName = userDataService.getString("Please enter a name of category: ");
        var shopName = userDataService.getString("Please enter a shop name: ");
        var countryName = userDataService.getString("Please enter a shop country: ");
        int quantity = userDataService.getInt("Please enter a quantity: ");

        return stockService.addProductToStock(productName, categoryName, shopName, countryName, quantity);
    }

    private CustomerOrderDto option6(Set<EPayment> ePayments, double discount) {
        var customerName = userDataService.getString("Please enter customer name: ");
        var customerSurname = userDataService.getString("Please enter customer surname: ");
        var countryName = userDataService.getString("Please enter the country: ");

        var productName = userDataService.getString("Please enter product name: ");
        var categoryName = userDataService.getString("Please enter product category name: ");

        var quantity = userDataService.getInt("Please enter the quantity of product you want to buy: ");
        var ldt = LocalDateTime.now();

        var customerOrderDto = CustomerOrderDto.builder()
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


    private Map<CategoryDto, Optional<ProductDto>> option7() {
        return productService.findProductsWithBiggestPriceInCategory();
    }

    private List<ProductDto> option8(String country, int ageTo, int ageFrom) {
        return productService.findAllProductsFromSpecificCountryBetweenCustomerAges(country, ageTo, ageFrom);
    }

    private List<ProductDto> option9(Set<EGuarantee> eGuarantees) {
        return productService.findAllProductsWithGivenGuarantees(eGuarantees);
    }

    private Set<ShopDto> option10() {
        return shopService.findAllShopsWithProductsWithCountryDifferentThanShopCountry();
    }

    private Set<ProducerDto> option11(String tradeName, Long quantity) {
        return producerService.findProducerWithGivenBrandAndTheBiggerQuantityProducedThanGiven(tradeName, quantity);
    }

    private List<CustomerOrderDto> option12(LocalDate dateFrom, LocalDate dateTo, BigDecimal price) {
        return customerOrderService.findOrdersBetweenDatesAndGivenPrice(dateFrom, dateTo, price);
    }

    private Map<ProducerDto, List<ProductDto>> option13(String customerName, String customerSurname, String countryName) {
        return customerOrderService.findProductsByCustomerAndHisCountry(customerName, customerSurname, countryName);
    }

    private Set<CustomerDto> option14() {
        return customerOrderService.findCustomersWhoOrderedProductWithSameCountryAsTheir();
    }

    private void option15() {
        var table = errorService.getTheMostErrorCausedTable();
        var message = errorService.getTheMostErrorMessage();
        var date = errorService.getTheMostErrorDate();

        System.out.println("\nTable " + table + " generated the most errors." +
                "\n" + message + " is the message of the error that appeared most often." +
                "\n" + date + " is the date for which the most errors occurred.");

    }
}
