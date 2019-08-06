package com.app.service.services;

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

    public void addCountry(CountryDto countryDTO) {
        countryValidator.validateCountry(countryDTO);

        Country country = countryRepository.findByName(countryDTO).orElse(null);
        if (country == null){
            country = Mappers.fromCountryDTOToCountry(countryDTO);
        }else{
            throw new MyException("COUNTRY WITH GIVEN NAME EXIST");
        }

        countryRepository.addOrUpdate(country);
    }


}
