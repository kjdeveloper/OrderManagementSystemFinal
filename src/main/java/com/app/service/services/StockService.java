package com.app.service.services;

import com.app.dto.CategoryDto;
import com.app.dto.ProductDto;
import com.app.dto.ShopDto;
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
import com.app.validation.impl.ProductValidator;
import com.app.validation.impl.ShopValidator;
import com.app.validation.impl.StockValidator;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.Map;

public class StockService {

    private final StockRepository stockRepository = new StockRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final ShopRepository shopRepository = new ShopRepositoryImpl();

    private final StockValidator stockValidator = new StockValidator();
    private final ShopValidator shopValidator = new ShopValidator();
    private final ProductValidator productValidator = new ProductValidator();

    public StockDto addProductToStock(ProductDto productDto, ShopDto shopDto, int quantity) {
        Product product = productRepository.findByName(productDto).orElse(null);
        Shop shop = shopRepository.findByName(shopDto).orElse(null);

        if (quantity <= 0) {
            throw new MyException("QUANTITY CAN NOT BE LESS OR EQUAL ZERO");
        }

        if (shop == null) {
            shopValidator.validateShop(shopDto);
            shop = Mappers.fromShopDTOToShop(shopDto);
            shop = shopRepository.addOrUpdate(shop).orElseThrow(() -> new MyException("CANNOT ADD SHOP IN STOCK"));
        }
        if (product == null) {
            productValidator.validateProduct(productDto);
            product = Mappers.fromProductDTOToProduct(productDto);
            product = productRepository.addOrUpdate(product).orElseThrow(() -> new MyException("CANNOT ADD PRODUCT IN STOCK"));
        }
        StockDto stockDto = StockDto.builder()
                .productDTO(productDto)
                .shopDTO(shopDto)
                .build();

        Stock stock = stockRepository.findStockByProductAndShop(stockDto).orElse(null);

        if (stock == null) {
            stock = Mappers.fromStockDTOToStock(stockDto);
        }

        stock.setProduct(product);
        stock.setShop(shop);
        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.addOrUpdate(stock);
        return Mappers.fromStockToStockDTO(stock);
    }


}
