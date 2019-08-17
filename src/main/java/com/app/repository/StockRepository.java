package com.app.repository;

import com.app.dto.StockDto;
import com.app.model.Stock;
import com.app.repository.generic.GenericRepository;

import java.util.Optional;

public interface StockRepository extends GenericRepository<Stock> {

    Optional<Stock> findStockByProductAndShop(StockDto stockDTO);

    int countProduct( Long productId );

}
