package com.app.mainmenu.menu;

import com.app.dto.CountryDto;
import com.app.dto.CustomerDto;
import com.app.service.AddService;
import com.app.service.UserDataService;

import java.util.Scanner;

public class MenuService {

    private final UserDataService userDataService = new UserDataService();
    private final AddService addService = new AddService();

    private Scanner sc = new Scanner(System.in);

    public MenuService() {
    }

    private void menu() {
        System.out.println("\nMENU");
        System.out.println("\nAdding methods");
        System.out.println("1.Add customer");
        System.out.println("2.");
        System.out.println("3.");
        System.out.println("4.");
        System.out.println("5.");
        System.out.println("6.");
        System.out.println("Downloads methods");
        System.out.println("8.");
        System.out.println("----------------------------------------------------");
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

                case 0:
                    userDataService.close();
                    System.out.println("Bye Bye");
                    return;
            }


        } while (true);
    }

    private CustomerDto option1(){
        System.out.println("Please enter a name of customer: ");
        String name = sc.next();
        System.out.println("Please enter a surname of customer: ");
        String surname = sc.next();
        System.out.println("Please enter the age of the customer: ");
        int age = sc.nextInt();
        System.out.println("Please enter the customer's country: ");
        String country = sc.next();

        CustomerDto customerDto = CustomerDto.builder()
                .name(name)
                .surname(surname)
                .age(age)
                .countryDTO(CountryDto.builder().name(country).build())
                .build();

        return addService.addCustomer(customerDto);
    }

}
