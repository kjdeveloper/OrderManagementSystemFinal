package com.app.service.services;

import com.app.dto.ShopDto;
import com.app.exceptions.MyException;
import com.app.model.Country;
import com.app.model.Shop;
import com.app.repository.CountryRepository;
import com.app.repository.ShopRepository;
import com.app.repository.impl.CountryRepositoryImpl;
import com.app.repository.impl.ShopRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CountryValidator;
import com.app.validation.impl.ShopValidator;

import java.util.List;

public class ShopService {

    private final ShopRepository shopRepository = new ShopRepositoryImpl();
    private final ShopValidator shopValidator = new ShopValidator();
    private final CountryValidator countryValidator = new CountryValidator();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();

    public ShopDto addShop(final ShopDto shopDTO) {
        shopValidator.validateShop(shopDTO);
        countryValidator.validateCountry(shopDTO.getCountryDto());

        final boolean exist = shopRepository.isExistByShopAndCountry(shopDTO);
        if (exist){
            throw new MyException("SHOP WITH GIVEN NAME AND COUNTRY IS ALREADY EXIST");
        }

        Country country = countryRepository.findByName(shopDTO.getCountryDto()).orElse(null);
        Shop shop = shopRepository.findByName(shopDTO).orElse(null);
        if (country == null){
            country = Mappers.fromCountryDtoToCountry(shopDTO.getCountryDto());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException("CANNOT ADD COUNTRY WITH SHOP"));
        }
        if (shop == null){
            shop = Mappers.fromShopDtoToShop(shopDTO);
        }

        shop.setCountry(country);
        shopRepository.addOrUpdate(shop);
        return Mappers.fromShopToShopDto(shop);
    }

    public List<ShopDto> findAllShopsWithProductsWithCountryDifferentThanShopsCountry() {
        return null;
    }
}
