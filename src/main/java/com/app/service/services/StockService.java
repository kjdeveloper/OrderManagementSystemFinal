package com.app.service.services;

import com.app.dto.ProductDto;
import com.app.dto.StockDto;
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

import java.util.HashSet;

public class StockService {

    private final StockRepository stockRepository = new StockRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final ShopRepository shopRepository = new ShopRepositoryImpl();

    public StockDto addProductToStock(String productName, String categoryName, String shopName, String countryName, int quantity) {
        if (productName == null) {
            throw new MyException("PRODUCT NAME IS NULL");
        }
        if (categoryName == null) {
            throw new MyException("CATEGORY NAME IS NULL");
        }
        if (shopName == null) {
            throw new MyException("SHOP NAME IS NULL");
        }
        if (countryName == null) {
            throw new MyException("COUNTRY NAME IS NULL");
        }
        if (quantity <= 0) {
            throw new MyException("QUANTITY IS LESS OR EQUAL 0");
        }

        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new MyException("PRODUCT WAS NOT FOUND. PLEASE ADD PRODUCT FIRST"));

        /*próbowałem juz zmapowac z jednaj na drugą ale nie pomaga
        ProductDto productDto = Mappers.fromProductToProductDto(product);
        product = Mappers.fromProductDtoToProduct(productDto);
       */

        Shop shop = shopRepository.findByName(shopName)
                .orElseThrow(() -> new MyException("SHOP WAS NOT FOUND. PLEASE ADD SHOP FIRST"));

        Stock stock = stockRepository.findStockByProductAndShop(productName, shopName)
                .orElse(null);
        product.setEGuarantees(new HashSet<>());
        //gdzie tu w stworzyc obiekt StockDto -> czy jest potrzebny
        //lazily initialize eguarantees...
        //dodaje prawidłowo
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
