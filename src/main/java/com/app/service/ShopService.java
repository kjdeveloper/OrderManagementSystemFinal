package com.app.service;

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

public class ShopService {

    private final ShopRepository shopRepository = new ShopRepositoryImpl();
    private final ShopValidator shopValidator = new ShopValidator();
    private final CountryService countryService = new CountryService();
    private final CountryValidator countryValidator = new CountryValidator();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();
    /*private boolean validateShop(ShopDto shopDTO) {

        Map<String, String> shopErrorsMap = shopValidator.validate(shopDTO);
        if (shopValidator.hasErrors()) {
            System.out.println("------SHOP VALIDATION ERRORS------");
            shopErrorsMap.forEach((k, v) -> System.out.println(k + " -> " + v));
            System.out.println("----------------------------------");
        }

        return !shopValidator.hasErrors();
    }*/

    public void addShop(final ShopDto shopDTO) {
        shopValidator.validateShop(shopDTO);
        countryValidator.validateCountry(shopDTO.getCountryDTO());

        final boolean exist = shopRepository.isExistByShopAndCountry(shopDTO);
        if (exist){
            throw new MyException("SHOP WITH GIVEN NAME AND COUNTRY IS ALREADY EXIST");
        }

        String countryName = shopDTO.getCountryDTO().getName();

        Country country = countryRepository.findByName(countryName).orElse(null);
        Shop shop = shopRepository.findByName(shopDTO.getName()).orElse(null);
        if (country == null){
            country = Mappers.fromCountryDTOToCountry(shopDTO.getCountryDTO());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException("CANNOT ADD COUNTRY WITH SHOP"));
        }
        if (shop == null){
            shop = Mappers.fromShopDTOToShop(shopDTO);
        }

        shop.setCountry(country);
        shopRepository.addOrUpdate(shop);
    }

}
