package com.app.repository;

import com.app.model.Customer;
import com.app.model.CustomerOrder;
import com.app.model.Product;
import com.app.repository.generic.GenericRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomerOrderRepository extends GenericRepository<CustomerOrder> {

    List<CustomerOrder> findProductsByCustomerAndHisCountry(String customerName, String customerSurname, String countryName);
}
