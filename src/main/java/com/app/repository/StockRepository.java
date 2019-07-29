package com.app.repository;

import com.app.dto.StockDto;
import com.app.model.Stock;
import com.app.repository.generic.GenericRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends GenericRepository<Stock> {
    List<Stock> findByQuantity(int quantity);

    Optional<Stock> findStockByProductAndShop(StockDto stockDTO);
}
