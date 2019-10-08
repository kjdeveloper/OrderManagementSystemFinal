package com.app.service.services;

import com.app.dto.StockDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Product;
import com.app.model.Shop;
import com.app.model.Stock;
import com.app.repository.ProductRepository;
import com.app.repository.ShopRepository;
import com.app.repository.StockRepository;
import com.app.repository.impl.ProductRepositoryImpl;
import com.app.repository.impl.ShopRepositoryImpl;
import com.app.repository.impl.StockRepositoryImpl;
import com.app.service.mapper.Mappers;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    public StockDto addProductToStock(String productName, String categoryName, String shopName, String countryName, int quantity) {
        if (productName == null) {
            throw new MyException(ExceptionCode.STOCK, "PRODUCT NAME IS NULL");
        }
        if (categoryName == null) {
            throw new MyException(ExceptionCode.STOCK, "CATEGORY NAME IS NULL");
        }
        if (shopName == null) {
            throw new MyException(ExceptionCode.STOCK, "SHOP NAME IS NULL");
        }
        if (countryName == null) {
            throw new MyException(ExceptionCode.STOCK, "COUNTRY NAME IS NULL");
        }
        if (quantity <= 0) {
            throw new MyException(ExceptionCode.STOCK, "QUANTITY IS LESS OR EQUAL ZERO");
        }

        var product = productRepository.findByName(productName)
                .orElseThrow(() -> new MyException(ExceptionCode.PRODUCT, "PRODUCT WAS NOT FOUND. PLEASE ADD PRODUCT FIRST"));

        var shop = shopRepository.findByName(shopName)
                .orElseThrow(() -> new MyException(ExceptionCode.SHOP, "SHOP WAS NOT FOUND. PLEASE ADD SHOP FIRST"));

        var stock = stockRepository.findStockByProductAndShop(productName, shopName)
                .orElse(null);

        product.setEGuarantees(new HashSet<>());

        if (stock == null){
            stock = Stock.builder()
                    .product(product)
                    .shop(shop)
                    .build();
        }

        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.addOrUpdate(stock);
        return Mappers.fromStockToStockDto(stock);
    }
}
