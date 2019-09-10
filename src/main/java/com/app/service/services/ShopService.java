package com.app.service.services;

import com.app.dto.CountryDto;
import com.app.dto.ProductDto;
import com.app.dto.ShopDto;
import com.app.dto.StockDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.repository.CountryRepository;
import com.app.repository.ShopRepository;
import com.app.repository.StockRepository;
import com.app.repository.impl.CountryRepositoryImpl;
import com.app.repository.impl.ShopRepositoryImpl;
import com.app.repository.impl.StockRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CountryValidator;
import com.app.validation.impl.ShopValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShopService {

    private final ShopRepository shopRepository = new ShopRepositoryImpl();
    private final ShopValidator shopValidator = new ShopValidator();
    private final CountryValidator countryValidator = new CountryValidator();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();
    private final StockRepository stockRepository = new StockRepositoryImpl();

    public ShopDto addShop(final ShopDto shopDTO) {
        shopValidator.validateShop(shopDTO);

        final boolean exist = shopRepository.isExistByShopAndCountry(shopDTO.getName(), shopDTO.getCountryDto().getName());
        if (exist) {
            throw new MyException(ExceptionCode.SHOP, "SHOP WITH GIVEN NAME AND COUNTRY IS ALREADY EXIST");
        }

        Country country = countryRepository.findByName(shopDTO.getCountryDto().getName()).orElse(null);

        if (country == null) {
            countryValidator.validateCountry(shopDTO.getCountryDto());
            country = Mappers.fromCountryDtoToCountry(shopDTO.getCountryDto());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException(ExceptionCode.COUNTRY, "CANNOT ADD COUNTRY WITH SHOP"));
        }

        Shop shop = shopRepository.findByName(shopDTO.getName()).orElse(null);
        if (shop == null) {
            shop = Mappers.fromShopDtoToShop(shopDTO);
        }

        shop.setCountry(country);
        shopRepository.addOrUpdate(shop);
        return Mappers.fromShopToShopDto(shop);
    }


/*
    public List<ShopDto> findAllShopsWithProductsWithCountryDifferentThanShopCountry() {
                stock
        return shopRepository.findAll()
                .stream()
                .map(Mappers::fromShopToShopDto)
                .flatMap(s -> new ArrayList<>(getAllProductsFromStocks(s.getId())))
                .map(ProductDto::getName)
                .collect(Collectors.toList());
    }*/
}
