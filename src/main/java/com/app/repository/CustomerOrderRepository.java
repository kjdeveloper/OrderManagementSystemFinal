package com.app.repository;

import com.app.model.CustomerOrder;
import com.app.repository.generic.GenericRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomerOrderRepository extends GenericRepository<CustomerOrder> {

    List<CustomerOrder> findOrdersBetweenDatesAndGivenPrice(LocalDateTime dateFrom, LocalDateTime dateTo, BigDecimal price);
}
