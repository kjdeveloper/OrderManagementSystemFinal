package com.app.service;

import com.app.dto.CountryDto;
import com.app.exceptions.MyException;
import com.app.model.Country;
import com.app.repository.CountryRepository;
import com.app.repository.impl.CountryRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CountryValidator;

import java.util.Map;

public class CountryService {

    private CountryRepository countryRepository = new CountryRepositoryImpl();
    private CountryValidator countryValidator = new CountryValidator();

    boolean validateCountry(CountryDto countryDTO) {

        Map<String, String> countryErrorsMap = countryValidator.validate(countryDTO);
        if (countryValidator.hasErrors()) {
            System.out.println("------COUNTRY VALIDATION ERRORS------");
            countryErrorsMap.forEach((k, v) -> System.out.println(k + " -> " + v));
            System.out.println("-------------------------------------");
        }
        return !countryValidator.hasErrors();
    }

    public void addCountry(CountryDto countryDTO) {
        countryValidator.validateCountry(countryDTO);

        Country country = countryRepository.findByName(countryDTO.getName()).orElse(null);
        if (country == null){
            country = Mappers.fromCountryDTOToCountry(countryDTO);
        }else{
            throw new MyException("COUNTRY WITH GIVEN NAME EXIST");
        }

        countryRepository.addOrUpdate(country);
    }


}
