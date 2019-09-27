package com.app.service.services;

import com.app.dto.CountryDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Country;
import com.app.repository.CountryRepository;
import com.app.repository.impl.CountryRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CountryValidator;

public class CountryService {

    private CountryRepository countryRepository = new CountryRepositoryImpl();
    private CountryValidator countryValidator = new CountryValidator();

    public CountryDto addCountry(CountryDto countryDTO) {
        countryValidator.validateCountry(countryDTO);

        var country = countryRepository.findByName(countryDTO.getName()).orElse(null);
        if (country == null){
            country = Mappers.fromCountryDtoToCountry(countryDTO);
        }else{
            throw new MyException(ExceptionCode.COUNTRY, "COUNTRY WITH GIVEN NAME EXIST");
        }

        countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException(ExceptionCode.COUNTRY, "CAN NOT AD COUNTRY IN COUNTRY SERVICE"));
        return Mappers.fromCountryToCountryDto(country);
    }


}
