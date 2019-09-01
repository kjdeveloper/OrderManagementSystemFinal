package com.app.validation.impl;

import com.app.dto.CountryDto;
import com.app.exceptions.MyException;

import java.util.Map;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class CountryValidator{

    public void validateCountry(final CountryDto countryDto){
        if (isNullOrEmpty(countryDto.getName())){
            throw new MyException("COUNTRY CAN NOT BE NULL OR EMPTY");
        }
        if (!isNameValid(countryDto)){
            throw new MyException("INVALID COUNTRY");
        }
    }

    private boolean isNameValid(final CountryDto countryDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return countryDTO.getName().matches(MODEL_NAME_REGEX);
    }


}
