package com.app.repository;

import com.app.dto.ProductDto;
import com.app.dto.StockDto;
import com.app.model.Producer;
import com.app.model.Shop;
import com.app.model.Stock;
import com.app.repository.generic.GenericRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends GenericRepository<Stock> {

    Optional<Stock> findStockByProductAndShop(String productName, String shopName);

    int countProduct( Long productId );

    List<Producer> findProducerWithGivenBrandAndTheBiggerQuantityProducedThanGiven(String tradeName, Long quantity);

    List<Stock> findShopWithSpecificProduct(Long productId);

}
